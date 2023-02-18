package istarwyh.moduleloader.constructor;

import com.google.common.base.CaseFormat;
import istarwyh.moduleloader.component.BaseDTO;
import istarwyh.moduleloader.component.BoardModule;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface BoardConstructor<T extends BoardModule<?>> {

    default Class<T> support(){
        Type[] genericInterfaces = this.getClass().getGenericInterfaces();
        for(Type type : genericInterfaces){
            if(type instanceof ParameterizedType &&
                    ((ParameterizedType) type).getRawType().getTypeName().equals(BoardConstructor.class.getName())){
                final Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
                return (Class<T>) actualTypeArgument;
            }
        }
        throw new IllegalArgumentException("Invalid interface");
    }

    T build(ViewStructure viewStructure, Object queryDTO);
}
