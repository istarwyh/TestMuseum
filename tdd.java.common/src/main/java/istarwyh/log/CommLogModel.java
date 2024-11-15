package istarwyh.log;

import static istarwyh.log.constant.CommLogErrorType.*;
import static istarwyh.log.constant.LogConstants.CN_LOG_TYPE_SEPARATOR;
import static istarwyh.log.constant.LogConstants.LOG_TYPE_SEPARATOR;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import istarwyh.log.annotation.NotPrintTrace;
import istarwyh.log.constant.CommLogErrorType;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

/**
 * @author xiaohui
 */
@Data
public class CommLogModel implements Serializable {

  private static final long serialVersionUID = 572934236155335956L;

  /** 是否需要转义日志内容中的“｜”，防止日志监控全部乱掉 */
  public static boolean ESCAPE_LOG_VERTICAL_LINE = false;

  private int requestMaxPrintLength = 2048;

  private int responseMaxPrintLength = 2048;

  /** 当前输出日志的方法名称 */
  private String classMethodName;

  /** 异常编码，最好是统一定义的异常编码，也可以是具体抛异常的方法名称，只要能具体表示该异常操作即可 */
  private String errorCode;

  /** 异常类型，，主要用于监控 {@link CommLogErrorType} */
  private String errorType;

  /** 异常信息，包括堆栈 */
  private String errorMsg;

  /** 业务类型 */
  private String bizType;

  /** traceId */
  private String traceId;

  /** 调用信息 */
  private String invokeInfo;

  /** 操作人信息 */
  private String operatorInfo;

  /** 入参内容 */
  private Map<String, Object> paramsMap;

  /** 上下文内容，例如中间变量，方便排查问题 */
  private Map<String, Object> contextMap;

  /** 上下文内容，例如中间变量，方便排查问题 */
  private List<Object> contextList;

  /** 返回值内容 */
  private Object returnValue;

  /** 影响条数 */
  private int count = 1;

  /** CommLog创建实践，用于计算RT */
  private long startTime;

  private Long rt;

  private String stat;

  /** 统计/分析信息，内容格式各个业务自己定义 */
  private List<String> statisticList;

  private Logger logger;

  private Consumer<String> loggerMethod;

  private String loggerLevel;

  public CommLogModel(String classMethodName, Logger logger) {
    this.classMethodName = classMethodName;
    this.logger = logger;
    this.loggerMethod = logger::info;

    initContent();
  }

  private void initContent() {
    contextMap = Maps.newHashMap();
    paramsMap = Maps.newHashMap();
    statisticList = Lists.newArrayList();
    contextList = Lists.newArrayList();
  }

  public CommLogModel clear() {
    initContent();
    return this;
  }

  public void setErrorType(Class<?> returnType, Object result) {
    if (!void.class.equals(returnType) && null == result) {
      this.setErrorType(RESULT_NULL);
    }
  }

  public void setErrorType(CommLogErrorType errorType) {
    this.setErrorType(errorType.toString());
  }

  public void setErrorType(String errorTypeName) {
    this.errorType = errorTypeName;
  }

  public String getParamsStr() {
    return StringUtils.substring(safelyToString(getParamsMap()), 0, getRequestMaxPrintLength());
  }

  public CommLogModel addContext(String name, Object value) {
    if (name == null) {
      return this;
    }
    if (value == null) {
      addContext(name);
    } else {
      contextMap.put(name, value);
    }
    return this;
  }

  public CommLogModel addContext(Object obj) {
    contextList.add(obj);
    return this;
  }

  public String getContextStr() {
    return StringUtils.substring(safelyToString(getContextMap()), 0, getRequestMaxPrintLength());
  }

  public String getContextListStr() {
    return StringUtils.substring(safelyToString(getContextList()), 0, getRequestMaxPrintLength());
  }

  public String getStatisticStr() {
    return safelyToString(getStatisticList());
  }

  public String getReturnValueStr() {
    return StringUtils.substring(safelyToString(getReturnValue()), 0, getResponseMaxPrintLength());
  }

  private String safelyToString(Object object) {
    if (object == null) {
      return null;
    }
    try {
      return JSON.toJSONString(object);
    } catch (Throwable ignore) {
      return object.toString();
    }
  }

  public boolean hitRule(boolean express, Object value) {
    if (express) {
      if (value instanceof String) {
        statisticList.add((String) value);
      } else {
        if (value != null) {
          statisticList.add(value.toString());
        }
      }
    }
    return express;
  }

  protected String escapeVerticalLine(String str) {
    if (!ESCAPE_LOG_VERTICAL_LINE) {
      return str;
    }
    if (StringUtils.isEmpty(str)) {
      return str;
    }
    return str.replaceAll("\\|", CN_LOG_TYPE_SEPARATOR);
  }

  protected static final ToStringStyle CUSTOM_PRINT_STYLE =
      new ToStringStyle() {
        {
          this.setContentStart("");
          this.setContentEnd("");
          this.setUseIdentityHashCode(false);
          this.setUseClassName(false);
          this.setUseFieldNames(false);
          this.setFieldSeparator(LOG_TYPE_SEPARATOR);
          this.setNullText("");
        }
      };

  @Override
  public String toString() {
    return new ToStringBuilder(null, CUSTOM_PRINT_STYLE)
        .append(getBizType())
        .append(getClassMethodName())
        .append(getTraceId())
        .append(getInvokeInfo())
        .append(escapeVerticalLine(getOperatorInfo()))
        .append(escapeVerticalLine(getParamsStr()))
        .append(escapeVerticalLine(getContextStr()))
        .append(escapeVerticalLine(getReturnValueStr()))
        .append(getStatisticStr())
        .append(getCount())
        .append(getRt())
        .append(getErrorCode())
        .append(getErrorType())
        .append(getErrorMsg())
        .toString()
        .replaceFirst("\\|$", "");
  }

  public void setErrorMsg(Throwable throwable) {
    if (null == throwable) {
      return;
    }
    Class<?> clazz = throwable.getClass();
    int level = 0;
    while (clazz != null && level < 3) {
      level++;
      if (null != clazz.getAnnotation(NotPrintTrace.class)) {
        this.errorMsg = getStackTraceByLevel(throwable, level);
        return;
      }
      clazz = clazz.getSuperclass();
    }
    StringWriter out = new StringWriter();
    throwable.printStackTrace(new PrintWriter(out));
    this.errorMsg = out.toString();
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  private String getStackTraceByLevel(Throwable throwable, int level) {
    StringBuilder sb = new StringBuilder("\n").append(throwable);
    int start = 0;
    for (StackTraceElement traceElement : throwable.getStackTrace()) {
      sb.append("\n\tat ").append(traceElement);
      if (++start >= level) {
        break;
      }
    }
    return sb.toString();
  }

  public Long getRt() {
    if (rt != null) {
      return rt;
    }
    if (Long.valueOf(0).equals(startTime)) {
      return null;
    }
    long endTime = System.currentTimeMillis();
    return endTime - startTime;
  }

  public int getCount() {
    if (returnValue instanceof List) {
      count = ((List<?>) returnValue).size();
    }
    return count;
  }

  public CommLogModel addParam(String parameterName, Object arg) {
    paramsMap.put(parameterName, arg);
    return this;
  }

  public CommLogModel warn() {
    this.loggerMethod = logger::warn;
    return this;
  }

  public void log() {
    initLoggerMethod();
    loggerMethod.accept(toString());
  }

  private void initLoggerMethod() {
    if (getErrorCode() != null || getErrorMsg() != null) {
      this.loggerMethod = logger::error;
      this.loggerLevel = "ERROR";
    } else if (RESULT_NULL.name().equals(getErrorType())
        || BIZ_WARN.name().equals(getErrorType())
        || BIZ_PROCESSING.name().equals(getErrorType())) {
      this.loggerMethod = logger::warn;
      this.loggerLevel = "WARN";
    } else {
      this.loggerMethod = logger::info;
      this.loggerLevel = "INFO";
    }
  }
}
