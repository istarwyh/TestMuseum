package istarwyh.log.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mac
 */
@Getter
@AllArgsConstructor
public enum CommLogErrorType {

    /**
     * 成功
     */
    SUCCESS("成功"),

    /**
     * 校验
     */
    ILLEGAL_REQUEST("非法请求"),

    ILLEGAL_ARGUMENT("非法参数"),

    /**
     * 不可重试错误
     */
    BIZ_ERROR("业务错误"),

    SYSTEM_ERROR("系统错误"),

    OUTER_ERROR("外部系统错误"),

    /**
     * 可重试错误
     */
    SENTINEL_BLOCK("限流异常"),

    /**
     * 有意忽略的错误
     */
    BIZ_WARN("业务警告"),

    BIZ_PROCESSING("业务处理中"),

    RESULT_NULL("结果为null"),

    /**
     * 兜底错误
     */
    UNKNOWN_ERROR("未知错误")

    ;


    private final String msg;
}
