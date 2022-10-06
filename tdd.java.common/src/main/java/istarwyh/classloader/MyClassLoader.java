package istarwyh.classloader;

import istarwyh.classloader.modifier.Modifier;
import javassist.ClassPool;
import javassist.CtClass;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * ClassLoader will load more class when needed. And {@link MyClassLoader} will load class what is not satisfied with
 * {@link MyClassLoader#shouldLoadedByParent(String)}.
 */
public class MyClassLoader extends ClassLoader{

    /**
     * some logic of modifying original class
     */
    private static final Set<Modifier> MODIFIERS;

    static {
        MODIFIERS = new HashSet<>(8);
        // JDK SPI load from META-INF.services/
        ServiceLoader.load(Modifier.class).forEach(Modifier::register);
    }

    /**
     * the second level package path as a special flag, like "tdd.args"
     */
    private static final String packageHead;
    static  {
        String packageName = MyClassLoader.class.getPackageName();
        String[] packageNameStrings = packageName.split("\\.");
        if(packageNameStrings.length > 1){
            String theSecondPackageNameInPackagePath = packageNameStrings[1];
            // extract the second level package path as a special flag, like "tdd.args"
            int endIndex = packageName.indexOf(theSecondPackageNameInPackagePath) + theSecondPackageNameInPackagePath.length();
            packageHead = packageName.substring(0, endIndex);
        }else {
            packageHead = null;
        }
    }

    public static void registerModifier(Modifier modifier){
        MODIFIERS.add(modifier);
    }

    @Override
    @SneakyThrows
    public Class<?> loadClass(String className, boolean resolve) {
        Class<?> loadedClass = findLoadedClass(className);
        if(loadedClass == null){
            return loadClassIfNotLoaded(className, resolve);
        }else {
            return resolveClass(loadedClass,resolve);
        }
    }

    @Override
    @SneakyThrows
    public Enumeration<URL> getResources(String name) {
        return super.getResources(name);
    }

    @SneakyThrows
    private Class<?> loadClassIfNotLoaded(String className, boolean resolve)  {
        if(shouldLoadedByMyself(className)){
            definePackageIfAbsent(className);
            return resolveClass(generateTargetClass(className), resolve);
        }else {
            return super.loadClass(className, resolve);
        }
    }

    /**
     * This method is designed to make more custom class to be loaded by {@link MyClassLoader} .
     * And This method use {@link MyClassLoader#packageHead} to accelerate the judgement logic.
     */
    private boolean shouldLoadedByMyself(String className){
        if(packageHead != null){
            return className.startsWith(packageHead) || !shouldLoadedByParent(className);
        }else {
            return !shouldLoadedByParent(className);
        }
    }

    /**
     * JVM ensures the class of type "java.*" must be loaded by Bootstrap ClassLoader.
     */
    private boolean shouldLoadedByParent(String className) {
        return className.startsWith("java.") ||
                className.startsWith("jdk.") ||
                className.startsWith("sun.") ||
                className.startsWith("javax.") ||
                className.startsWith("com.sun.") ||
                className.startsWith("com.intellij.") ||
                className.startsWith("org.junit.") ||
                className.startsWith("org.slf4j.") ||
                className.startsWith("ch.qos.") ||
                className.startsWith("net.") ||
                className.startsWith("org.mockito.") ;
    }

    private Class<?> generateTargetClass(String className) {
        String resourcePath = className.replace('.', '/').concat(".class");
        URL url = getResources(resourcePath).nextElement();
        byte[] targetLoadedClassBytes = handleLoadedClass(className, url);
        return defineClass(className,targetLoadedClassBytes,0,targetLoadedClassBytes.length);
    }

    /**
     * handle the loadedClass info with the logic of the {@link Modifier}
     */
    @SneakyThrows
    private static byte[] handleLoadedClass(String className, URL url) {
        try(InputStream in = url.openStream()){
            if(in == null){
                return new byte[0];
            }else {
                CtClass ctClass = Singleton.classPool.makeClass(in);
                MODIFIERS.forEach(it -> it.afterLoadClass(className,ctClass));
                return ctClass.toBytecode();
            }
        }
    }

    private void definePackageIfAbsent(String className) {
        synchronized (this){
            int i = className.lastIndexOf('.');
            if(i != -1){
                String pkgName = className.substring(0,i);
                if(getPackage(pkgName) == null){
                    definePackage(pkgName,null,null,null,
                            null,null,null,null);
                }
            }
        }
    }

    private Class<?> resolveClass(Class<?> loadedClass,boolean resolve) {
        if(resolve){
            resolveClass(loadedClass);
        }
        return loadedClass;
    }

    private static class Singleton{
        public static ClassPool classPool = new ClassPool();
        static {
            classPool.appendSystemPath();
        }
    }

    @Override
    public String getName() {
        return "my";
    }
}
