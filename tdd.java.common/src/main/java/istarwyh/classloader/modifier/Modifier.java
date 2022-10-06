package istarwyh.classloader.modifier;

import istarwyh.classloader.MyClassLoader;
import javassist.CtClass;


public interface Modifier {

    /**
     * choose which classloader should use this modifier
     */
    default void register(){
        MyClassLoader.registerModifier(this);
    }

    /**
     * the method will wipe the current class and all of its superclass except the Class of Object
     * @param className the method invoke of ctClass.getClassFile() is banned, so there must be className as a parameter
     * @param ctClass  ctClass of the class called className
     */
    void afterLoadClass(String className, CtClass ctClass);
}
