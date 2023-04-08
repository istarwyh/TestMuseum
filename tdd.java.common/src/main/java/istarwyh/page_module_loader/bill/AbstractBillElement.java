package istarwyh.page_module_loader.bill;

import com.google.common.base.CaseFormat;
import istarwyh.page_module_loader.PageModule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractBillElement<DATA> extends BillElementDTO implements PageModule<DATA> {

    private DATA data;

    public String getModuleTypeCode(){
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, this.getClass().getSimpleName());
    }

    @Override
    public DATA getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = (DATA) data;
    }
}
