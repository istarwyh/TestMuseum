package istarwyh.log;

import java.lang.annotation.*;

/**
 * @author mac
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommLog {

    /**
     * 是否在切面中打印日志
     */
    boolean print() default true;
}
