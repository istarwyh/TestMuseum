package istarwyh.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: IllegalAnnotationException
 * @Author: wx:istarwyh
 * @Date: 2022-05-29 23:30
 * @Version: ing
 */
@AllArgsConstructor
public class IllegalAnnotationException extends RuntimeException{
    @Getter
    private final String option;
}
