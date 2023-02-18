package istarwyh.moduleloader.component;

import com.google.common.base.CaseFormat;

public interface BoardModule<T> {

    /**
     *
     * @return 标志模块类型的Code
     *
     */
    String getModuleTypeCode();

    T getData();

    void setData(Object data);
}
