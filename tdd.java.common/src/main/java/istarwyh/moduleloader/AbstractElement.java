package istarwyh.moduleloader;

import istarwyh.moduleloader.util.NameConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractElement<T> extends ElementDTO implements PageModule<T>{

    public String getModuleTypeCode(){
        if(super.getModuleTypeCode() != null){
            return super.getModuleTypeCode();
        }
        return NameConverter.toUpperUnderScoreName(this.getClass().getSimpleName());
    }

}
