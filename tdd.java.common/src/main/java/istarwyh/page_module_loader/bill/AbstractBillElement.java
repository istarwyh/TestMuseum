package istarwyh.page_module_loader.bill;

import com.google.common.base.CaseFormat;
import istarwyh.page_module_loader.PageModule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;


/**
 * @author xiaohui
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractBillElement<DATA> extends BillElementDTO implements PageModule<DATA> {

    private DATA data;

    @Override
    public String getModuleTypeCode(){
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, this.getClass().getSimpleName());
    }

    @Override
    public @NotNull DATA getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        this.data = (DATA) data;
    }
}
