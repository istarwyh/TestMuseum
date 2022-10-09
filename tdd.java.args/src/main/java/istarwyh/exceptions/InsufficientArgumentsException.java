package istarwyh.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: InsufficientArgumentsException
 * @Author: wx:istarwyh
 * @Date: 2022-05-29 21:58
 * @Version: ing
 */
@AllArgsConstructor
public class InsufficientArgumentsException extends RuntimeException{

    @Getter
    private final String option;
}
