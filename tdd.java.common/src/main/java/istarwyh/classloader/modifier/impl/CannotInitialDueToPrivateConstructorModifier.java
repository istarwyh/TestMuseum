package istarwyh.classloader.modifier.impl;

import istarwyh.classloader.model.CannotInitialLackMatchedConstructor;
import istarwyh.classloader.modifier.Modifier;
import istarwyh.util.JavassistUtil;
import javassist.CtClass;

public class CannotInitialDueToPrivateConstructorModifier implements Modifier {

    @Override
    public void afterLoadClass(String className, CtClass ctClass) {
        Class<?> startClass = CannotInitialLackMatchedConstructor.class;
        if(startClass.getName().equals(className)){
            while (startClass != Object.class){
                JavassistUtil.addNoArgsConstructorIfAbsent(ctClass);
                startClass = startClass.getSuperclass();
            }
        }
    }
}
