基于`Slf4j`接口的打印日志风格自由度很高，导致团队内部打印日志风格百花齐放，不免出现
- 查询、阅读日志困难
- 埋点缺失
- 分隔符（`,`、`#`、`|`、`.`等）不统一，导致 `xflush` 等工具配置监控无法通用截取和采样；有些分隔符也不是默认的分词符号，导致搜索失效
- 日志位置不统一，消费日志分析、监控每次都要重新配
- 错误堆栈缺失

基于此，可以从`Slf4j` 自己定义统一的日志打印API：
- `@Slf4j` -> `@CustomLog`
- `lombok.extern.slf4j.Slf4j` -> `lombok.CustomLog`
- `org.slf4j.Logger` -> `your.Logger`
- `org.slf4j.LoggerFactory` -> `your.LoggerFactory`
- 配置 `lombok.config`
- 如果使用logback日志框架，配置 `logback-spring.xml`

然后可以只使用自己的 `info` 、 `warn` 、`error`这些API。


不过这些日志打印还都有一个缺点，那就是所有有效的日志内容都会打印到 content 中成为一个字符串，而不是输出的时候就是结构化的。 虽然日志监控工具一般提供了日志切分的功能，从而不影响日志监控。但不是结构化的日志就是比较难看。