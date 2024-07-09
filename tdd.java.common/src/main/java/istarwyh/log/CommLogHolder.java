package istarwyh.log;

import static istarwyh.log.constant.LogConstants.CLASS_METHOD_SEPARATOR;

import com.google.common.collect.Maps;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mac
 */
public class CommLogHolder {

  private static final ThreadLocal<Map<String, CommLogModel>> HOLDER = new ThreadLocal<>();
  private static final Logger logger = LoggerFactory.getLogger(CommLogHolder.class);

  public static void put(String classMethodName, CommLogModel commLogModel) {
    if (null == HOLDER.get()) {
      Map<String, CommLogModel> map = Maps.newConcurrentMap();
      map.put(classMethodName, commLogModel);
      HOLDER.set(map);
    } else {
      HOLDER.get().put(classMethodName, commLogModel);
    }
  }

  public static CommLogModel get(String classMethodName, Logger logger) {
    if (classMethodName == null) {
      return get();
    }
    CommLogModel logModel = HOLDER.get().get(classMethodName);
    if (logModel == null) {
      logModel = new CommLogModel(classMethodName, logger);
    }
    CommLogHolder.put(classMethodName, logModel);
    return logModel;
  }

  public static CommLogModel get() {
    StackTraceElement[] stackTrace = new Exception().getStackTrace();
    StackTraceElement stackTraceElement = getStackTraceElement(stackTrace);
    String fileName = stackTraceElement.getFileName();
    String methodName = stackTraceElement.getMethodName();
    if (fileName == null) {
      return buildDummyCommLogModel(methodName);
    }
    String classMethod = fileName.split("\\.")[0] + CLASS_METHOD_SEPARATOR + methodName;
    if (HOLDER.get() == null || HOLDER.get().get(classMethod) == null) {
      CommLogHolder.put(classMethod, new CommLogModel(classMethod, logger));
    }
    return HOLDER.get().get(classMethod);
  }

  @NotNull
  private static CommLogModel buildDummyCommLogModel(String methodName) {
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
