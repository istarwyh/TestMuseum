package istarwyh.log;

import static istarwyh.log.constant.LogConstants.CLASS_METHOD_SEPARATOR;

import com.alibaba.fastjson2.JSONObject;
import istarwyh.log.annotation.CommLog;
import istarwyh.log.annotation.LogIgnore;
import istarwyh.log.annotation.LogShow;
import istarwyh.util.TypeUtils;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author mac {@link EnableAspectJAutoProxy}
 */
public abstract class AbstractLogInterceptor {

  protected boolean ignoreCommLog(ProceedingJoinPoint joinPoint) {
    boolean print;
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
    CommLogModel logModel =
        CommLogHolder.get(getClassMethodName(joinPoint, methodSignature), logger);
    logModel.setTraceId(getTraceId());
    logModel.setInvokeInfo(getRpcId());
    CommLog commLog = methodSignature.getMethod().getAnnotation(CommLog.class);
    addLogModelParam(logModel, joinPoint, commLog);
    Object result = proceedResultMaybeLogError(joinPoint, logModel);
    // 根据业务具体的Result 类型获取信息 补全 errorCode errorMsg 和 errorType
    postCustomResult(result, logModel);
    logModel.setReturnValue(result);
    logModel.setErrorType(methodSignature, result);
    logModel.log();
    return result;
  }

  @NotNull
  private static String getClassMethodName(
      ProceedingJoinPoint joinPoint, MethodSignature methodSignature) {
    return joinPoint.getTarget().getClass().getSimpleName()
        + CLASS_METHOD_SEPARATOR
        + methodSignature.getMethod().getName();
  }

  private static void addLogModelParam(
      CommLogModel logModel, ProceedingJoinPoint joinPoint, CommLog commLog) {
    String[] parameterNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();

    Object[] args = joinPoint.getArgs();
    if (parameterNames.length == args.length) {
      for (int i = 0; i < parameterNames.length; i++) {
        if (Objects.nonNull(args[i])) {
          String parameterName = parameterNames[i];
          if (commLog != null && Arrays.asList(commLog.ignoreParams()).contains(parameterName)) {
            continue;
          }
          logModel.addParam(parameterName, handleArg(args[i]));
        }
      }
    }
  }

  private static Object handleArg(Object args) {
    if (args == null) {
      return new JSONObject();
    }
    if (TypeUtils.cannotGetAccess(args.getClass())) {
      return args;
    }
    try {
      return convertJsonObject(args);
    } catch (Throwable throwable) {
      LoggerFactory.getLogger(AbstractLogInterceptor.class).error("convert args to jsonObject error:",throwable);
      return args;
    }
  }

  private static Object convertJsonObject(Object args) throws IllegalAccessException {
    JSONObject jsonObject = new JSONObject();
    boolean hasLogShow = false;
    Field[] fields = args.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (field.isAnnotationPresent(LogShow.class)) {
        hasLogShow = true;
        break;
      }
    }
    for (Field field : fields) {
      field.setAccessible(true);
      String fieldName = field.getName();
      Object value = field.get(args);
      if (hasLogShow) {
        if (field.isAnnotationPresent(LogShow.class)) {
          jsonObject.put(fieldName, value);
        }
      } else {
        if (!field.isAnnotationPresent(LogIgnore.class)) {
          jsonObject.put(fieldName, value);
        }
      }
    }
    return jsonObject;
  }

  protected abstract String getRpcId();

  protected abstract String getTraceId();

  private Object proceedResultMaybeLogError(ProceedingJoinPoint joinPoint, CommLogModel logModel) {
    Object result;
    try {
      result = joinPoint.proceed();
    } catch (Throwable e) {
      postThrowableResult(e, logModel);
      logModel.log();
      throw new RuntimeException(e);
    } finally {
      CommLogHolder.clear(logModel.getClassMethodName());
    }
    return result;
  }

  /**
   * postThrowableResult
   *
   * @param e {@link Throwable}
   * @param logModel {@link CommLogModel}
   */
  abstract void postThrowableResult(Throwable e, CommLogModel logModel);

  /**
   * postCustomResult
   *
   * @param result result
   * @param logModel {@link CommLogModel}
   */
  abstract void postCustomResult(Object result, CommLogModel logModel);

  private static boolean ifPrint(CommLog commLog) {
    return commLog != null && commLog.print();
  }
}
