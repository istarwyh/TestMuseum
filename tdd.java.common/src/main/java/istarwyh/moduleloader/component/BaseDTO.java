package istarwyh.moduleloader.component;

import com.alibaba.fastjson2.JSON;
import com.google.common.base.CaseFormat;
import istarwyh.moduleloader.display.SubjectCodeEnum;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
public class BaseDTO {

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
     * 标志模块类型的Code
     */
    private String moduleTypeCode;


    public BaseDTO(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getModuleTypeCode() {
        return Objects.requireNonNullElseGet(moduleTypeCode,
                () -> CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, this.getClass().getSimpleName()));
    }


    public static BaseDTO of(SubjectCodeEnum subjectCode, String amount, String number) {
        BaseDTO baseDTO = new Point(subjectCode.name());
        baseDTO.setAmount(amount);
        baseDTO.setNumber(number);
        return baseDTO;
    }

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }
}
