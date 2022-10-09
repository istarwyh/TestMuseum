package istarwyh.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: TooManyArgumentsException
 * @Author: wx:istarwyh
 * @Date: 2022-05-22 23:28
 * @Version: ing
 */
@AllArgsConstructor
public class TooManyArgumentsException extends RuntimeException{

    @Getter
    private final String option;
}
