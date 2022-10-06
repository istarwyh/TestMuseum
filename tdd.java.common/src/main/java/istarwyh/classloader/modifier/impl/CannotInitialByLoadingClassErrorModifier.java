package istarwyh.classloader.modifier.impl;

import istarwyh.classloader.model.CannotInitialDueToLoadingClassError;
import istarwyh.classloader.modifier.Modifier;
import javassist.CtClass;
import lombok.SneakyThrows;

import static istarwyh.util.JavassistUtil.wipeOriginalByteCode;

public class CannotInitialByLoadingClassErrorModifier implements Modifier {

    @Override
    @SneakyThrows
    public void afterLoadClass(String className, CtClass ctClass) {
        Class<?> startClass = CannotInitialDueToLoadingClassError.class;
        if(startClass.getName().equals(className)){
            while (startClass != Object.class){
                wipeOriginalByteCode(ctClass);
                startClass = startClass.getSuperclass();
            }
        }
    }
}
