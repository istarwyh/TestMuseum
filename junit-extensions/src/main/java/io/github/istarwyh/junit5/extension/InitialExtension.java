package io.github.istarwyh.junit5.extension;

import static io.github.istarwyh.util.UnsafeUtils.unsafe;
import static java.util.Arrays.stream;

import io.github.istarwyh.classloader.MyClassLoader;
import io.github.istarwyh.classloader.modifier.Modifier;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;

/**
 * This junit5 extension is designed for new instance for any custom class (not JDK)
 * by create its implementation of {@link Modifier}
 * Usually these are the following situation what needs hack the class:
 * 1. the class depends on some native runtime environment like cpp17, but your machine cannot support it
 * 2. the class was just designed to be loaded by the specified class rather than AppClassloader
 * @author xiaohui
 * @warning Don't support ParameterizedTest && Nested Test
 * @warning If you don't know about contextClassLoader, don't use {@code Thread.currentThread().setContextClassLoader(MY_CLASS_LOADER)}.
 * <p>The thread contextClassLoader is used to break the Bi-Parent Delegation Mechanism. If you do not set anything,
 * the contextClassloader for Java application threads defaults to the system contextClassloader.
 * By using the thread contextClassloader in the code of the SPI interface,
 * you can successfully load the class into the SPI implementation. But in this situation, we just want the scope of
 * this extension can be limited to the current class.</p>
 */
public class InitialExtension implements BeforeAllCallback
        , BeforeEachCallback
        , TestExecutionExceptionHandler
        , LifecycleMethodExecutionExceptionHandler,ParameterResolver {

    private static final ClassLoader MY_CLASS_LOADER;
    static {
        MY_CLASS_LOADER = new MyClassLoader();
    }

    private static Class<?> newTestClass;
    private static final String SET_UP_METHOD_NAME = "setUp";

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        String name = context.getRequiredTestClass().getName();
        newTestClass = MY_CLASS_LOADER.loadClass(name);
        System.out.println(name + " are loaded by " + newTestClass.getClassLoader().getClass().getName());
    }

    /**
     * Why use reflection instead of generating the custom testInstance?
     * @see <a href="https://github.com/junit-team/junit5/issues/201>Introduce">extension API for providing a custom ClassLoader (e.g., for Powermock)</a>
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Object instance = unsafe().allocateInstance(newTestClass);
        invokeSetUpMethod(instance, context.getRequiredTestClass().getDeclaredMethods());
        invokeNewTestMethod(instance, context.getRequiredTestMethod());
    }

    private static void invokeNewTestMethod(Object instance, Method testMethod) throws IllegalAccessException, InvocationTargetException {
        Method newTestMethod = getNewTestMethod(testMethod);
        newTestMethod.setAccessible(true);
        if(newTestMethod.getParameterCount() == 0){
            newTestMethod.invoke(instance);
        }else {
            // todo There is ParameterizedTest what can be got in testMethod.
            throw new UnsupportedOperationException("ParameterizedTest cannot be supported at present!");
        }
    }

    private static Method getNewTestMethod(Method testMethod) {
        Method newTestMethod;
        String testMethodName = testMethod.getName();
        Class<?>[] parameterTypes = testMethod.getParameterTypes();
        try{
            newTestMethod = newTestClass.getDeclaredMethod(testMethodName, parameterTypes);
        }catch (NoSuchMethodException ignore){
            // todo Sometimes the above getDeclaredMethod will fail without unknown reason.
            newTestMethod = stream(newTestClass.getDeclaredMethods())
            .filter(it -> it.getName().equals(testMethodName))
            .findFirst()
            .orElseThrow(() -> new RuntimeException(String.format("Cannot find method: %s ,parameterTypes: %s", testMethodName, Arrays.toString(parameterTypes))));
        }
        return newTestMethod;
    }

    private static void invokeSetUpMethod(Object instance, Method[] declaredMethods) {
        stream(declaredMethods)
                .filter(it -> SET_UP_METHOD_NAME.equals(it.getName()))
                .findFirst()
                .ifPresent(
                        setUpMethod -> {
                            boolean existBeforeEachOnSetupMethod = stream(setUpMethod.getDeclaredAnnotations())
                                    .anyMatch(it -> BeforeEach.class.equals(it.annotationType()));
                            if(existBeforeEachOnSetupMethod){
                                stream(newTestClass.getDeclaredMethods())
                                        .filter(it -> SET_UP_METHOD_NAME.equals(it.getName()))
                                        .findFirst()
                                        .ifPresent(it -> {
                                                    it.setAccessible(true);
                                                    try {
                                                        it.invoke(instance);
                                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                        );
                            }
                        }
                );

    }

    /**
     * don't throw throwable because actual invoke happened by reflection
     * @param context the current extension context; never {@code null}
     * @param throwable the {@code Throwable} to handle; never {@code null}
     */
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) {}

    /**
     * don't throw throwable because actual invoke happened by reflection
     * @param context the current extension context; never {@code null}
     * @param throwable the {@code Throwable} to handle; never {@code null}
     */
    @Override
    public void handleAfterEachMethodExecutionException(ExtensionContext context, Throwable throwable){}

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return false;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return null;
    }
}
