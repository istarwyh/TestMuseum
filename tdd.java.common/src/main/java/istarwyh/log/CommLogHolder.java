package istarwyh.log;

import static istarwyh.log.constant.LogConstants.CLASS_METHOD_SEPARATOR;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mac
 */
public class CommLogHolder {

  private static final ThreadLocal<Map<String, CommLogModel>> HOLDER = new ThreadLocal<>();
  private static final String COMMLOG = "COMMLOG";

  /**
   * 假设每个线程中大约包含 32 个类 + 方法
   *
   * @param classMethodName 类 + 方法名称
   * @param commLogModel 日志对象模型
   */
  public static void put(String classMethodName, CommLogModel commLogModel) {
    if (null == HOLDER.get()) {
      Map<String, CommLogModel> map = new ConcurrentHashMap<>(32);
      map.put(classMethodName, commLogModel);
      HOLDER.set(map);
    } else {
      HOLDER.get().put(classMethodName, commLogModel);
    }
  }

  public static CommLogModel getIfAbsentThenPut(String classMethodName, Logger logger) {
    if (classMethodName == null) {
      StackTraceElement[] stackTrace = new Exception().getStackTrace();
      return parseStackAndIfAbsentThenPut(stackTrace, logger);
    } else {
      CommLogModel commLogModel = new CommLogModel(classMethodName, logger);
      absentThenPut(commLogModel);
      return commLogModel;
    }
  }

  public static CommLogModel commLog() {
    StackTraceElement[] stackTrace = new Exception().getStackTrace();
    return parseStackAndIfAbsentThenPut(stackTrace, LoggerFactory.getLogger(COMMLOG));
  }

  public static CommLogModel customLog(Logger logger) {
    StackTraceElement[] stackTrace = new Exception().getStackTrace();
    return parseStackAndIfAbsentThenPut(stackTrace, logger);
  }

  /**
   * 新建一个空白的 LogModel
   * 默认的logModel只是 classMethodName + 线程唯一，当一个线程在循环中调用同一个方法打印日志，可以使用该方法避免日志模型被污染
   */
  public static CommLogModel newLog(Logger logger) {
    StackTraceElement[] stackTrace = new Exception().getStackTrace();
    CommLogModel logModel = parseStackAndIfAbsentThenPut(stackTrace, logger);
    return logModel.clear();
  }

  /**
   * 单纯的 logModel,不做缓存等操作
   */
  public static CommLogModel logModel() {
    StackTraceElement[] stackTrace = new Exception().getStackTrace();
    return buildCommLogModel(stackTrace, LoggerFactory.getLogger(COMMLOG));
  }

  private static CommLogModel parseStackAndIfAbsentThenPut(
      StackTraceElement[] stackTrace, Logger logger) {
    CommLogModel logModel = buildCommLogModel(stackTrace, logger);
    absentThenPut(logModel);
    return logModel;
  }

  @NotNull
  private static CommLogModel buildCommLogModel(StackTraceElement[] stackTrace, Logger logger) {
    StackTraceElement stackTraceElement = getStackTraceElement(stackTrace);
    String fileName = stackTraceElement.getFileName();
    String methodName = stackTraceElement.getMethodName();
    if (fileName == null) {
      return buildDummyCommLogModel(methodName, logger);
    } else {
      String classMethod = fileName.split("\\.")[0] + CLASS_METHOD_SEPARATOR + methodName;
      return new CommLogModel(classMethod, logger);
    }
  }

  private static void absentThenPut(CommLogModel commLogModel) {
    String classMethodName = commLogModel.getClassMethodName();
    if (HOLDER.get() == null || HOLDER.get().get(classMethodName) == null) {
      CommLogHolder.put(classMethodName, commLogModel);
    }
  }

  @NotNull
  private static CommLogModel buildDummyCommLogModel(String methodName, Logger logger) {
    String lackClassMethodName = "DummyLogClass" + CLASS_METHOD_SEPARATOR + methodName;
    CommLogModel logModel = new CommLogModel(lackClassMethodName, logger);
    CommLogHolder.put(lackClassMethodName, logModel);
    return logModel;
  }

  private static StackTraceElement getStackTraceElement(StackTraceElement[] stackTrace) {
    StackTraceElement stackTraceElement;
    if (stackTrace.length > 0) {
      stackTraceElement = stackTrace[1];
    } else {
      stackTraceElement = Thread.getAllStackTraces().get(Thread.currentThread())[3];
    }
    return stackTraceElement;
  }

  public static void clear(String classMethod) {
    Map<String, CommLogModel> logModelMap = HOLDER.get();
    if (logModelMap != null) {
      logModelMap.remove(classMethod);
    }
    if (logModelMap != null && logModelMap.isEmpty()) {
      HOLDER.remove();
    }
  }
}
