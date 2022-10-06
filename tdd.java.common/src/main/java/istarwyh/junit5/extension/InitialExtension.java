package istarwyh.junit5.extension;

import istarwyh.classloader.MyClassLoader;
import istarwyh.classloader.modifier.Modifier;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static istarwyh.util.UnsafeUntil.unsafe;


/**
 * This junit5 extension is designed for new instance for any custom class (not JDK)
 * by create its implementation of {@link Modifier}
 * Usually these are the following situation what needs hack the class:
 * 1. the class depends on some native runtime environment like cpp17, but your machine cannot support it
 * 2. the class was just designed to be loaded by the specified class rather than AppClassloader
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
        , LifecycleMethodExecutionExceptionHandler {

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
        System.out.println(name + " are loaded by " + newTestClass.getClassLoader().getName());
    }

    /**
     * Why use reflection instead of generating the custom testInstance?
     * @see <a href="https://github.com/junit-team/junit5/issues/201>Introduce">extension API for providing a custom ClassLoader (e.g., for Powermock)</a>
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Object instance = unsafe().allocateInstance(newTestClass);
        invokeSetUpMethod(context.getRequiredTestClass(),instance);
        Method testMethod = context.getRequiredTestMethod();
        Method newTestMethod = newTestClass.getDeclaredMethod(testMethod.getName(), testMethod.getParameterTypes());
        newTestMethod.setAccessible(true);
        if(newTestMethod.getParameterCount() == 0){
            newTestMethod.invoke(instance);
        }else {
            throw new IllegalCallerException("ParameterizedTest cannot be supported at present!");
        }
    }

    @SneakyThrows
    private static void invokeSetUpMethod(Class<?> testClass,Object instance) {
        Arrays.stream(testClass.getDeclaredMethods())
                .filter(it -> SET_UP_METHOD_NAME.equals(it.getName()))
                .findFirst()
                .ifPresent(
                        setUpMethod -> {
                            boolean existBeforeEachOnSetupMethod = Arrays.stream(setUpMethod.getDeclaredAnnotations())
                                    .anyMatch(it -> BeforeEach.class.equals(it.annotationType()));
                            if(existBeforeEachOnSetupMethod){
                                Arrays.stream(newTestClass.getDeclaredMethods())
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
}
