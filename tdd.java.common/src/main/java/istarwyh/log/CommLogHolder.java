package istarwyh.log;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author mac
 */
public class CommLogHolder {

  private static final ThreadLocal<Map<String, CommLogModel>> holder = new ThreadLocal<>();

  public static void put(String classMethod, CommLogModel commLogModel) {
    if (null == holder.get()) {
      Map<String, CommLogModel> map = Maps.newConcurrentMap();
      map.put(classMethod, commLogModel);
      holder.set(map);
    } else {
      holder.get().put(classMethod, commLogModel);
    }
  }

  public static CommLogModel get() {
    StackTraceElement[] stackTrace = new Exception().getStackTrace();
    StackTraceElement stackTraceElement;
    if (stackTrace.length > 0) {
      stackTraceElement = stackTrace[1];
    } else {
      stackTraceElement = Thread.getAllStackTraces().get(Thread.currentThread())[3];
    }
    String fileName = stackTraceElement.getFileName();
    String methodName = stackTraceElement.getMethodName();
    if (fileName == null) {
      return new CommLogModel("#" + methodName);
    }
    String classMethod =
        stackTraceElement.getFileName().split("\\.")[0] + "#" + stackTraceElement.getMethodName();
    if (holder.get() == null || holder.get().get(classMethod) == null) {
      return new CommLogModel(classMethod);
    }
    return holder.get().get(classMethod);
  }

  public static void clear(String classMethod) {
    Map<String, CommLogModel> logModelMap = holder.get();
    if (logModelMap != null) {
      logModelMap.remove(classMethod);
    }
    if (logModelMap != null && logModelMap.isEmpty()) {
      holder.remove();
    }
  }
}
