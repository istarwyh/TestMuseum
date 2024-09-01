package istarwyh.log;

import static istarwyh.log.constant.LogConstants.CLASS_METHOD_SEPARATOR;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import com.alibaba.fastjson2.JSONObject;
import istarwyh.log.annotation.CommLog;
import istarwyh.log.annotation.LogIgnore;
import istarwyh.log.annotation.LogShow;
import istarwyh.util.TypeUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author mac {@link EnableAspectJAutoProxy} 使用示例:
 *     <pre>{@code
 *  @Around("@within(istarwyh.log.annotation.CommLog) || @annotation(istarwyh.log.annotation.CommLog)")
 *  public Object aroundCommLog(ProceedingJoinPoint joinPoint) throws Throwable {
 *     CommLog commLog = findCommLog(joinPoint);
 *     if (commLog == null) {
 *       return joinPoint.proceed();
 *     }
 *     return logWith(joinPoint, commLog);
 * }
 *
 * }</pre>
 */
public abstract class AbstractLogInterceptor {

  protected CommLog findCommLog(ProceedingJoinPoint joinPoint) {
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    CommLog commLog = methodSignature.getMethod().getAnnotation(CommLog.class);
    if (commLog != null) {
      return commLog;
    }
    return joinPoint.getTarget().getClass().getAnnotation(CommLog.class);
  }

  /**
   * 通过 {@link ProceedingJoinPoint} 和 {@link CommLog} 打印日志 <br>
   * 其中 {@link Logger} 通过 {@link CommLog} 指定
   */
  public Object logWith(ProceedingJoinPoint joinPoint, CommLog commLog) {
    Logger logger = LoggerFactory.getLogger(commLog.loggerName());
    return logWith(joinPoint, commLog, logger);
  }

  /**
   * 通过 {@link ProceedingJoinPoint}、{@link CommLog}和{@link Logger}打印日志 <br>
   * 其中 {@link Logger} 通过 {@link CommLog} 指定
   */
  public Object logWith(ProceedingJoinPoint joinPoint, CommLog commLog, Logger logger) {
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    String classSimpleName = joinPoint.getTarget().getClass().getSimpleName();
    String methodName = methodSignature.getMethod().getName();
    String[] ignoreParams = commLog.ignoreParams();
    boolean notThrow = commLog.notThrow();

    CommLogModel logModel =
        CommLogHolder.getIfAbsentThenPut(getClassMethodName(classSimpleName, methodName), logger);
    logModel.setRequestMaxPrintLength(getRequestMaxPrintLength(logModel));
    logModel.setResponseMaxPrintLength(getResponseMaxPrintLength(logModel));
    logModel.setTraceId(getTraceId());
    logModel.setInvokeInfo(getRpcId());
    addLogModelParam(logModel, joinPoint, ignoreParams);
    Object result =
        notThrow
            ? proceedResultNotThrow(joinPoint, logModel)
            : proceedResultMayThrow(joinPoint, logModel);
    postCustomResult(result, logModel);
    logModel.setReturnValue(result);
    logModel.setErrorType(methodSignature.getReturnType(), result);
    log(logModel);
    return result;
  }

  protected void log(CommLogModel logModel) {
    logModel.log();
  }

  protected int getResponseMaxPrintLength(CommLogModel logModel) {
    return logModel.getResponseMaxPrintLength();
  }

  protected int getRequestMaxPrintLength(CommLogModel logModel) {
    return logModel.getRequestMaxPrintLength();
  }

  @NotNull
  private static String getClassMethodName(String classSimpleName, String methodName) {
    return classSimpleName + CLASS_METHOD_SEPARATOR + methodName;
  }

  private static void addLogModelParam(
      CommLogModel logModel, ProceedingJoinPoint joinPoint, String[] ignoreParams) {
    String[] parameterNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
    Object[] args = joinPoint.getArgs();
    List<String> ignoreParamList = Arrays.asList(ignoreParams);

    if (parameterNames.length == args.length) {
      for (int i = 0; i < parameterNames.length; i++) {
        if (Objects.nonNull(args[i])) {
          String parameterName = parameterNames[i];
          if (ignoreParamList.contains(parameterName)) {
            continue;
          }
          logModel.addParam(parameterName, handleArg(args[i]));
        }
      }
    }
  }

  /**
   * 遍历对象的所有字段，如果有 {@link LogShow} 注解,只加入这些字段; 如果没有 {@link LogShow} 注解，则忽略带有 {@link LogIgnore}
   * 注解的字段，其他字段全部加入到 JSONObject 中
   *
   * @param args 传入的方法参数
   * @return 处理后的方法参数
   */
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
      LoggerFactory.getLogger(AbstractLogInterceptor.class)
          .error("convert args to jsonObject error:", throwable);
      return args;
    }
  }

  private static Object convertJsonObject(Object args) throws IllegalAccessException {
    JSONObject jsonObject = new JSONObject();
    boolean hasLogShow = false;
    List<Field> fields = getAllDeclaredFields(args.getClass());
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

  @NotNull
  private static List<Field> getAllDeclaredFields(Class<?> clazz) {
    List<Field> fields = new ArrayList<>();
    for (Class<?> currentClass = clazz;
        currentClass != null && !currentClass.equals(Object.class);
        currentClass = currentClass.getSuperclass()) {
      fields.addAll(stream(currentClass.getDeclaredFields()).collect(toList()));
    }
    return fields;
  }

  protected abstract String getRpcId();

  protected abstract String getTraceId();

  private Object proceedResultNotThrow(ProceedingJoinPoint joinPoint, CommLogModel logModel) {
    Object result;
    try {
      long startTime = System.currentTimeMillis();
      logModel.setStartTime(startTime);
      result = joinPoint.proceed();
      logModel.setRt(System.currentTimeMillis() - startTime);
    } catch (Throwable e) {
      postThrowableResult(e, logModel);
      return logModel.getReturnValue();
    } finally {
      CommLogHolder.clear(logModel.getClassMethodName());
    }
    return result;
  }

  private Object proceedResultMayThrow(ProceedingJoinPoint joinPoint, CommLogModel logModel) {
    Object result;
    try {
      long startTime = System.currentTimeMillis();
      logModel.setStartTime(startTime);
      result = joinPoint.proceed();
      logModel.setRt(System.currentTimeMillis() - startTime);
    } catch (Throwable e) {
      postThrowableResult(e, logModel);
      logModel.setReturnValue(null);
      log(logModel);
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
}
