package istarwyh.page_module_loader;

import com.alibaba.fastjson2.JSON;
import istarwyh.page_module_loader.bill.AbstractBillElement;
import istarwyh.page_module_loader.bill.BillElementDTO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author xiaohui
 */
public interface PageModuleConstructor<Element extends AbstractBillElement<?>, QueryDTO> {

    /**
     *
     * @return supported page module
     */
    @NotNull
    default Class<Element> supportedElement(){
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
     * eg. build with {@link BillElementDTO}
     * @param pageModuleRawStructure {@link PageModuleRawStructure}
     * @param context {@link DataContext}
     * @return {@link PageModule}
     */
    default Element construct(PageModuleRawStructure pageModuleRawStructure, DataContext<QueryDTO> context){
        Element element = JSON.parseObject(pageModuleRawStructure.getStructureStr(), this.supportedElement());
        BillElementDTO billElementDTO = context.getElement(element.getSubjectCode());
        if(billElementDTO == null){
            return element;
        }
        List<BillElementDTO> details = billElementDTO.getDetails();
        if(details != null && !details.isEmpty()){
            element.setData(details);
        }
        element.setAmount(billElementDTO.getAmount());
        element.setNumber(billElementDTO.getNumber());
        element.setTime(billElementDTO.getTime());
        return element;
    }

    /**
     * register the page module constructor in a map by SPI
     */
    default void register(){
        PageModuleLoader.registerPageModuleConstructor(this);
    }
}
