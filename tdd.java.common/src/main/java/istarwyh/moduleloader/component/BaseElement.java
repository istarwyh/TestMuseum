package istarwyh.moduleloader.component;

import com.google.common.base.CaseFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class BaseElement {

    /**
     * 标志数据的Code
     */
    private String theCode;

    /**
     * 金额
     */
    private String amount;

    /**
     * 数量
     */
    private String number;

    /**
     * 时间
     */
    private String time;

    /**
     * 标志模块类型的Code
     */
    private String moduleTypeCode;

    /**
     * 子项
     */
    private List<BaseElement> details;

    public String getModuleTypeCode(){
        if(this.getClass() == BaseElement.class || moduleTypeCode != null){
            return moduleTypeCode;
        }
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, this.getClass().getSimpleName());
    }

}
