package istarwyh.log;

import istarwyh.log.annotation.CommLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Objects;

import static istarwyh.log.CommLogErrorType.RESULT_NULL;

/**
 * @author mac
 */
public abstract class AbstractLogInterceptor {

  protected boolean ignoreCommLog(ProceedingJoinPoint joinPoint) {
    boolean print = false;
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    CommLog commLog = methodSignature.getMethod().getAnnotation(CommLog.class);
    print = ifPrint(commLog);
    if (!print) {
      commLog = joinPoint.getTarget().getClass().getAnnotation(CommLog.class);
      print = ifPrint(commLog);
    }
    return !print;
  }

  public Object buildToCommLog(ProceedingJoinPoint joinPoint, Logger logger) {
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    String classAndMethodName =
        joinPoint.getTarget().getClass().getSimpleName()
            + "#"
            + methodSignature.getMethod().getName();
    CommLogModel logModel = new CommLogModel(classAndMethodName);
    logModel.setTraceId("");
    logModel.setRpcId("");

    String[] parameterNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
    Object[] args = joinPoint.getArgs();
    if (parameterNames.length == args.length) {
      for (int i = 0; i < parameterNames.length; i++) {
        if (Objects.isNull(args[i])) {
          String parameterName = parameterNames[i];
          CommLog commLog = methodSignature.getMethod().getAnnotation(CommLog.class);
          if (commLog != null && Arrays.asList(commLog.ignoreParams()).contains(parameterName)) {
            continue;
          }
          logModel.addParam(parameterName, args[i]);
        }
      }
    }
    CommLogHolder.put(classAndMethodName, logModel);
    Object result = null;
    try {
      result = joinPoint.proceed();
    } catch (Throwable e) {
      postThrowableResult(e, logModel);
      logger.info(logModel.toString());
      throw new RuntimeException(e);
    } finally {
      CommLogHolder.clear(classAndMethodName);
    }
    postCustomResult(methodSignature.getReturnType(), result, logModel);
    //TODO 通过 logModel 中的 LEVEL 来判断打印什么级别的日志
    logger.info(logModel.toString());
    return result;
  }

  abstract void postThrowableResult(Throwable e, CommLogModel logModel);

  private void postCustomResult(Class<?> returnType, Object result, CommLogModel logModel) {
    if (!void.class.equals(returnType) && null == result) {
      logModel.setErrorType(RESULT_NULL);
    }
    // 根据业务具体的Result 类型获取信息 补全 errorCode errorMsg 和 errorType
    postCustomResult(result, logModel);
    logModel.setReturnValue(result);
  }

  abstract void postCustomResult(Object result, CommLogModel logModel);

  private static boolean ifPrint(CommLog commLog) {
    return commLog != null && commLog.print();
  }
}
