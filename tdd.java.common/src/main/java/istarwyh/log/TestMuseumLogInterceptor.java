package istarwyh.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author mac
 */
@Aspect
@Component
@Order(1)
public class TestMuseumLogInterceptor extends AbstractLogInterceptor{

    private static final Logger COMMLOG = LoggerFactory.getLogger("COMMLOG");

    @Override
    void postThrowableResult(Throwable e, CommLogModel logModel) {

    }

    @Override
    void postCustomResult(Object result, CommLogModel logModel) {

    }

    @Around("execution(* istarwyh.handler..*.*(..)")
    public Object aroundHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        if(ignoreCommLog(joinPoint)){
            return joinPoint.proceed();
        }
        return buildToCommLog(joinPoint, LoggerFactory.getLogger("HANDLER"));
    }

    @Around("@within(istarwyh.log.annotation.CommLog) || @annotation(istarwyh.log.annotation.CommLog)")
    public Object aroundCommLog(ProceedingJoinPoint joinPoint) throws Throwable {
        if(ignoreCommLog(joinPoint)){
            return joinPoint.proceed();
        }

        return buildToCommLog(joinPoint, COMMLOG);
    }
}
