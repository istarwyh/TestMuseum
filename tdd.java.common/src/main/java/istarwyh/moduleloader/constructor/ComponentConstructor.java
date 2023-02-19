package istarwyh.moduleloader.constructor;

import com.alibaba.fastjson2.JSON;
import istarwyh.moduleloader.component.BoardModule;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface ComponentConstructor<T extends BoardModule<?>> {

    default Class<T> support(){
        Type[] genericInterfaces = this.getClass().getGenericInterfaces();
        for(Type type : genericInterfaces){
            if(type instanceof ParameterizedType &&
                    ((ParameterizedType) type).getRawType().getTypeName().equals(ComponentConstructor.class.getName())){
                final Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
                return (Class<T>) actualTypeArgument;
            }
        }
        throw new IllegalArgumentException("Invalid interface");
    }

    default T build(ViewStructure viewStructure, Object queryDTO){
        return JSON.parseObject(viewStructure.getStructureStr(), this.support());
    }
}
