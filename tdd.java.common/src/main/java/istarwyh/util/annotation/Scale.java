package istarwyh.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.RoundingMode;

/**
 * @author xiaohui
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Scale {
    /**
     *
     * @return the scale
     */
    int value() default 2;

    /**
     *
     * @return the roundingMode
     */
    RoundingMode roundingMode() default RoundingMode.HALF_EVEN;
}
