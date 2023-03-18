package istarwyh.moduleloader;

import com.google.common.base.CaseFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractElement<T> extends ElementDTO implements PageModule<T>{

    private T data;

    public String getModuleTypeCode(){
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, this.getClass().getSimpleName());
    }

    @Override
    public T getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = (T) data;
    }
}
