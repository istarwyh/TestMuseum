package istarwyh.page_module_loader;

import lombok.Data;

import java.util.List;

/**
 * @author xiaohui
 */
@Data
public class ElementDTO {

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
    private List<ElementDTO> details;

}
