package istarwyh.moduleloader;

import com.alibaba.fastjson2.JSON;
import jakarta.validation.constraints.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface PageModuleConstructor<T extends PageModule<?>,Q> {

    /**
     *
     * @return supported page module
     */
    @NotNull
    default Class<T> support(){
        Type[] genericInterfaces = this.getClass().getGenericInterfaces();
        for(Type type : genericInterfaces){
            if(type instanceof ParameterizedType &&
                    ((ParameterizedType) type).getRawType().getTypeName().equals(PageModuleConstructor.class.getName())){
                Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
                if(actualTypeArgument != null){
                    return getClassFromParameterizedType(actualTypeArgument);
                }
            }
        }
        throw new IllegalArgumentException("Invalid interface");
    }

    @Nullable
    private static <T>  Class<T> getClassFromParameterizedType(Type actualTypeArgument) {
        if(actualTypeArgument instanceof Class){
            return (Class<T>) actualTypeArgument;
        }else {
            return getClassFromParameterizedType(((ParameterizedType)actualTypeArgument).getRawType());
        }
    }

    /**
     *
     * @param viewStructure {@link ViewStructure}
     * @param context {@link DataContext}
     * @return {@link PageModule}
     */
    default T construct(ViewStructure viewStructure, DataContext<Q> context){
        return JSON.parseObject(viewStructure.getStructureStr(), this.support());
    }

    /**
     * register the page module constructor in a map by SPI
     */
    default void register(){
        ModuleLoader.registerPageModuleConstructor(this);
    }
}
