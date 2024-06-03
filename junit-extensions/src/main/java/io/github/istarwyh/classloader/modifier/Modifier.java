package io.github.istarwyh.classloader.modifier;



import io.github.istarwyh.classloader.MyClassLoader;

import io.github.istarwyh.util.ReflectionUtils;
import javassist.CannotCompileException;
import javassist.CtClass;
import lombok.SneakyThrows;

import static io.github.istarwyh.util.JavassistUtil.wipeOriginalByteCode;


/**
 * @author xiaohui
 */
public interface Modifier<T> {

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
    @SneakyThrows(CannotCompileException.class)
    default void afterLoadClass(String className, CtClass ctClass){
        Class<?> needBeModifiedClass = ReflectionUtils.<T>getInterfaceFirstGenericClazz(Modifier.class, this.getClass());
        if(needBeModifiedClass.getName().equals(className)){
            while (needBeModifiedClass != Object.class){
                wipeOriginalByteCode(ctClass);
                needBeModifiedClass = needBeModifiedClass.getSuperclass();
            }
        }
    }
}
