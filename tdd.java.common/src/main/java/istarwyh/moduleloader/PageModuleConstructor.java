package istarwyh.moduleloader;

import com.alibaba.fastjson2.JSON;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public interface PageModuleConstructor<T extends AbstractElement<?>,Q> {

    /**
     *
     * @return supported page module
     */
    @NotNull
    default Class<T> supportedClass(){
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
        T t = JSON.parseObject(viewStructure.getStructureStr(), this.supportedClass());
        ElementDTO baseElement = context.getElement(t.getSubjectCode());
        if(baseElement == null){
            return t;
        }
        List<ElementDTO> details = baseElement.getDetails();
        if(CollectionUtils.isNotEmpty(details)){
            t.setData(details);
        }
        t.setAmount(baseElement.getAmount());
        t.setNumber(baseElement.getNumber());
        t.setTime(baseElement.getTime());
        return t;
    }

    /**
     * register the page module constructor in a map by SPI
     */
    default void register(){
        ModuleLoader.registerPageModuleConstructor(this);
    }
}
