package istarwyh.exception;

/**
 * @author xiaohui
 * 错误本身是函数返回值的一部分
 */
public class ErrorModel {

    /**
     * 错误码，唯一标识该错误类型，域内唯一
     * 所有对外错误码应该公开，可以直接定位到报错位置
     */
    int errorCode;

    /**
     * 错误所属的领域、服务、模块
     */
    String domain;

    /**
     * 错误直接的信息
     */
    String errorMessage;

    /**
     * 错误堆栈，用于排查根本原因
     */
    String[] errorStacks;
}
