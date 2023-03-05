package istarwyh.moduleloader.constructor;

import com.alibaba.fastjson2.JSON;
import istarwyh.moduleloader.component.PageModule;
import istarwyh.moduleloader.display.ModuleLoader;
import jakarta.validation.constraints.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface ComponentConstructor<T extends PageModule<?>> {

    @NotNull
    default Class<T> support(){
        Type[] genericInterfaces = this.getClass().getGenericInterfaces();
        for(Type type : genericInterfaces){
            if(type instanceof ParameterizedType &&
                    ((ParameterizedType) type).getRawType().getTypeName().equals(ComponentConstructor.class.getName())){
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

    default T build(ViewStructure viewStructure, ModuleLoader.DataContext context){
        return JSON.parseObject(viewStructure.getStructureStr(), this.support());
    }
}
