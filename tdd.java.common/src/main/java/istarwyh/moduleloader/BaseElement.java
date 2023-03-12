package istarwyh.moduleloader;

import istarwyh.moduleloader.util.NameConverter;
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
    private String subjectCode;

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
        return NameConverter.toUpperUnderScoreName(this.getClass().getSimpleName());
    }

}
