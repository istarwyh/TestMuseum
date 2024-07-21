package istarwyh.log;

import istarwyh.log.annotation.CommLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Order;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author mac
 */
@Aspect
@Component
@Order(1)
public class TestMuseumLogInterceptor extends AbstractLogInterceptor {

  @Override
  protected String getRpcId() {
    return null;
  }

  @Override
  protected String getTraceId() {
    return null;
  }

  @Override
  void postThrowableResult(Throwable e, CommLogModel logModel) {}

  @Override
  void postCustomResult(Object result, CommLogModel logModel) {}

  @Around("execution(* istarwyh.handler.*.*(..))")
  public Object aroundHandler(ProceedingJoinPoint joinPoint) throws Throwable {
    CommLog commLog = findCommLog(joinPoint);
    if (commLog == null) {
      return joinPoint.proceed();
    }

    return logWith(joinPoint, commLog, LoggerFactory.getLogger("FACADE"));
  }

  @Around(
      "@within(istarwyh.log.annotation.CommLog) || @annotation(istarwyh.log.annotation.CommLog)")
  public Object aroundCommLog(ProceedingJoinPoint joinPoint) throws Throwable {
    CommLog commLog = findCommLog(joinPoint);
    if (commLog == null) {
      return joinPoint.proceed();
    }
    return logWith(joinPoint, commLog);
  }
}
