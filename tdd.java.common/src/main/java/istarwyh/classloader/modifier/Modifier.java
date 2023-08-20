package istarwyh.classloader.modifier;

import istarwyh.classloader.MyClassLoader;
import javassist.CtClass;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static istarwyh.util.JavassistUtil.wipeOriginalByteCode;


public interface Modifier<T> {

    /**
     * Returns the modified class
     * @return the modified class
     */
    @NotNull
    default Class<T> modifiedClass(){
        Type[] genericInterfaces = this.getClass().getGenericInterfaces();
        for(Type type : genericInterfaces){
            if(type instanceof ParameterizedType &&
                    ((ParameterizedType) type).getRawType().getTypeName().equals(Modifier.class.getName())){
                Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
                if(actualTypeArgument != null){
                    return getClassFromParameterizedType(actualTypeArgument);
                }
            }
        }
        throw new IllegalArgumentException("Invalid interface");
    }

    @NotNull
    private static <T>  Class<T> getClassFromParameterizedType(Type actualTypeArgument) {
        if(actualTypeArgument instanceof Class){
            return (Class<T>) actualTypeArgument;
        }else {
            return getClassFromParameterizedType(((ParameterizedType)actualTypeArgument).getRawType());
        }
    }

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
    @SneakyThrows
    default void afterLoadClass(String className, CtClass ctClass){
        Class<?> startClass = this.modifiedClass();
        if(startClass.getName().equals(className)){
            while (startClass != Object.class){
                wipeOriginalByteCode(ctClass);
                startClass = startClass.getSuperclass();
            }
        }
    }
}
