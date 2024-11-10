package istarwyh.log.constant;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * @author mac
 */
public class LogConstants {

  /** 日志类型切分符 英文版 */
  public static final String LOG_TYPE_SEPARATOR = "|";

  /** 日志类型切分符 中文版 */
  public static final String CN_LOG_TYPE_SEPARATOR = "｜";

  /** 同key 多值切分，KV 与 KV 之间分隔符 */
  public static final String KV_PAIR_SEPARATOR = ",";

  /** 同key KV 之间分隔符 */
  public static final String KV_SEPARATOR = ":";

  /** 类与方法之间分隔符 */
  public static final String CLASS_METHOD_SEPARATOR = ".";

    /**
     * 日志时间打印格式 "YYYY-MM-DD HH-mm-ss.sss"
     */
  public static final DateTimeFormatter LOG_ISO_LOCAL_DATE_TIME_FORMAT =
      new DateTimeFormatterBuilder()
          .parseCaseInsensitive()
          .append(DateTimeFormatter.ISO_LOCAL_DATE)
          .appendLiteral(" ")
          .append(DateTimeFormatter.ISO_LOCAL_TIME)
          .toFormatter();
}
