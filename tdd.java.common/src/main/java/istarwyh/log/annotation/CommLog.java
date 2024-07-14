package istarwyh.log.annotation;

import java.lang.annotation.*;

/**
 * @author mac
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommLog {

    /**
     * 是否在切面中拦截异常，默认不拦截
     * 不拦截的情况适用于非Controller 的情况
     */
    boolean catchException() default false;

    /**
     * 忽略打日志的参数名称
     */
    String[] ignoreParams() default "";

}
