package istarwyh.log;

import istarwyh.log.constant.LogConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;

import java.time.LocalDateTime;

/**
 * @author xiaohui 对于SLS(Simple Log Service) 来说，输出到SLS的就是一行大字符串，此外没有传统机器日志中默认会输出的时间、 Logger LEVEL还有环境信息
 *     INFO 这些
 */
public abstract class SlsLogModel extends CommLogModel {


  public SlsLogModel(String classMethodName, Logger logger) {
    super(classMethodName, logger);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(null, CUSTOM_PRINT_STYLE)
        .append(LocalDateTime.now().format(LogConstants.LOG_ISO_LOCAL_DATE_TIME_FORMAT))
        .append(getLoggerLevel())
        .append(getEnv())
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

    /**
   * This method is intended to return the environment information related to the log.
   *
   * @return A string representing the environment information. This could be the name of the environment (e.g., "production", "staging", "development"),
   *         the version of the application, or any other relevant environment-specific details.
   *         The returned string should be formatted in a way that it can be easily parsed and used for further analysis or filtering.
   */
  protected abstract String getEnv();
}
