package istarwyh.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: IllegalValueException
 * @Author: wx:istarwyh
 * @Date: 2022-06-18 17:45
 * @Version: ing
 */
@AllArgsConstructor
public class IllegalValueException extends RuntimeException{

    @Getter
    private final String option;

    /**
     * 在解析第一个参数时就应该报错，所以这里的value有值时应为第一个参数
     */
    @Getter
    private final String value;
}
