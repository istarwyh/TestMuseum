package istarwyh.log;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mac
 */
@Getter
@AllArgsConstructor
public enum CommLogErrorCode {

    SUCCESS("00000","成功"),
    USER_ERROR("A0001","用户端错误"),
    SYSTEM_ERROR("B0001","系统错误"),
    THIRD_PARTY_ERROR("C0001","调用的三方错误")

    ;

    private final String code;
    private final String msg;
}
