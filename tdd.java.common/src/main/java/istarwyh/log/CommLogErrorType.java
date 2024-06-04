package istarwyh.log;

import lombok.AllArgsConstructor;

/**
 * @author mac
 */
@AllArgsConstructor
public enum CommLogErrorType {

    REQUEST_ILLEGAL("非法请求"),

    PARAM_INVALID("无效参数"),

    BIZ_WARN("业务警告"),

    BIZ_IGNORE("业务忽略"),

    BIZ_PROCESSING("业务处理中"),

    OUTER_ERROR("外部系统错误"),

    SENTINEL_BLOCK("限流异常"),

    UNKNOWN_ERROR("未知错误")

    ;

    private final String msg;
}
