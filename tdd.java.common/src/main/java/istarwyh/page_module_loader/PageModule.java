package istarwyh.page_module_loader;

import org.jetbrains.annotations.NotNull;

/**
 * @author xiaohui
 */
public interface PageModule<T> {

    /**
     * getModuleTypeCode
     * @return 标志模块类型的Code
     *
     */
    String getModuleTypeCode();

    /**
     *
     * @return 标志元素信息的Code
     */
    String getSubjectCode();

    /**
     * getData
     * @return  data
     */
    @NotNull
    T getData();

    /**
     * setData
     * @param data data
     */
    void setData(Object data);
}
