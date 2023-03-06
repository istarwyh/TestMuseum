package istarwyh.moduleloader.component;

public interface PageModule<T> {

    /**
     *
     * @return 标志模块类型的Code
     *
     */
    String getModuleTypeCode();

    /**
     *
     * @return 标志元素信息的Code
     */
    String getSubjectCode();

    T getData();

    void setData(Object data);
}
