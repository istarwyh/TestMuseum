package istarwyh.log;

import static istarwyh.log.constant.CommLogErrorType.*;
import static istarwyh.log.constant.LogConstants.CN_LOG_TYPE_SEPARATOR;
import static istarwyh.log.constant.LogConstants.LOG_TYPE_SEPARATOR;

import com.alibaba.fastjson2.JSON;
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
import org.aspectj.lang.reflect.MethodSignature;
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

  /** 返回值内容 */
  private Object returnValue;

  /** 影响条数 */
  private int count = 1;

  /** CommLog创建实践，用于计算RT */
  private long startTime;

  private Long rt;

  private String stat;

  /** 统计/分析信息，内容格式各个业务自己定义 */
  private Map<String, String> statisticMap;

  private Logger logger;

  private Consumer<String> loggerMethod;

  public CommLogModel(String classMethodName, Logger logger) {
    this.classMethodName = classMethodName;
    this.startTime = System.currentTimeMillis();
    this.logger = logger;
    this.loggerMethod = logger::info;

    contextMap = Maps.newHashMap();
    paramsMap = Maps.newHashMap();
    statisticMap = Maps.newHashMap();
  }

  public void setErrorType(MethodSignature methodSignature, Object result) {
    if (!void.class.equals(methodSignature.getReturnType()) && null == result) {
      this.setErrorType(RESULT_NULL);
    }
  }

  public void setErrorType(CommLogErrorType errorType) {
    this.errorType = errorType.name();
  }

  public String getParams() {
    if (getParamsMap() == null) {
      return "";
    }
    return StringUtils.substring(JSON.toJSONString(getParamsMap()), 0, getRequestMaxPrintLength());
  }

  public CommLogModel addContext(String name, Object value) {
    contextMap.put(name, value);
    return this;
  }

  public String getContext() {
    if (getParamsMap() == null) {
      return "";
    }
    return StringUtils.substring(JSON.toJSONString(getContextMap()), 0, getRequestMaxPrintLength());
  }

  public String getStat() {
    if (getStatisticMap() == null) {
      return "";
    }
    return JSON.toJSONString(getStatisticMap());
  }

  public String getReturnValueStr() {
    if (returnValue == null) {
      return null;
    }
    return StringUtils.substring(JSON.toJSONString(returnValue), 0, getResponseMaxPrintLength());
  }

  public boolean addStatHitRule(boolean express, String value) {
    if (express) {
      statisticMap.put("hitRule", value);
    }
    return express;
  }

  private String escapeVerticalLine(String str) {
    if (!ESCAPE_LOG_VERTICAL_LINE) {
      return str;
    }
    if (StringUtils.isEmpty(str)) {
      return str;
    }
    return str.replaceAll("\\|", CN_LOG_TYPE_SEPARATOR);
  }

  private static final ToStringStyle CUSTOM_PRINT_STYLE =
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
        .append(getClassMethodName())
        .append(getTraceId())
        .append(getInvokeInfo())
        .append(escapeVerticalLine(getOperatorInfo()))
        .append(escapeVerticalLine(getParams()))
        .append(escapeVerticalLine(getContext()))
        .append(escapeVerticalLine(getReturnValueStr()))
        .append(getStatisticMap())
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
    if (getErrorCode() != null || getErrorMsg() != null) {
      this.loggerMethod = logger::error;
    } else if (RESULT_NULL.name().equals(getErrorType())
        || BIZ_IGNORE.name().equals(getErrorType())
        || BIZ_WARN.name().equals(getErrorType())
        || BIZ_PROCESSING.name().equals(getErrorType())) {
      this.loggerMethod = logger::warn;
    }
    loggerMethod.accept(toString());
  }
}
