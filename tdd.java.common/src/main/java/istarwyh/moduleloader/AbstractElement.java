package istarwyh.moduleloader;

import istarwyh.moduleloader.util.NameConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractElement<T> extends ElementDTO implements PageModule<T>{

    private T data;

    public String getModuleTypeCode(){
        if(super.getModuleTypeCode() != null){
            return super.getModuleTypeCode();
        }
        return NameConverter.toUpperUnderScoreName(this.getClass().getSimpleName());
    }

    @Override
    public T getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = (T) data;
    }
}
