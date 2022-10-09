package istarwyh.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: UnsupportedOptionTypeException
 * @Author: wx:istarwyh
 * @Date: 2022-06-19 10:42
 * @Version: ing
 */
@AllArgsConstructor
public class UnsupportedOptionTypeException extends RuntimeException{

    @Getter
    private final String option;

    @Getter
    private final Class<?> type;
}
