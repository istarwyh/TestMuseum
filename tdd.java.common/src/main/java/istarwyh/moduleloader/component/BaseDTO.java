package istarwyh.moduleloader.component;

import com.google.common.base.CaseFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;


@Data
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
}
