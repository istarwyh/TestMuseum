package istarwyh.log.annotation;

import java.lang.annotation.*;

/**
 * @author mac
 * 注解的属性必须是编译时常量表达式
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommLog {

  /** 是否在切面中拦截异常，默认不拦截 不拦截的情况适用于非Controller 的情况 */
  boolean notThrow() default false;

  /** 忽略打日志的参数名称
   *  默认值为空的字符串数组
   * */
  String[] ignoreParams() default {};

  /** 这里 loggerName 用于 构造{@link org.slf4j.LoggerFactory#getLogger(String)} 的名字 */
  String loggerName() default "COMMLOG";
}
