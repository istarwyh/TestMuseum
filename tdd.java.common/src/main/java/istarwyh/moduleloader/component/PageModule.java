package istarwyh.moduleloader.component;

public interface PageModule<T> {

    /**
     *
     * @return 标志模块类型的Code
     *
     */
    String getModuleTypeCode();

    T getData();

    void setData(Object data);
}
