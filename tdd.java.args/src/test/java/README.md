
[小结](https://time.geekbang.org/column/article/494604)
>至此，我们使用 TDD 的方法完成了参数解析的功能。我觉得你至少应该感受到了 TDD 这三个特点。

>第一是，将要完成的功能分解成一系列任务，再将任务转化为测试，以测试体现研发进度，将整个开发过程变成有序的流程，以减少无效劳动。

>第二是，在修改代码的时候，随时执行测试以验证功能，及时发现错误，降低发现、定位错误的成本，降低修改错误的难度。

>第三是，时刻感受到认知的提升，增强自信降低恐惧。在针对列表参数使用任务分解法时，你明显可以感觉到，我们无论是对需求的把握性，还是对最终实现的可预见性，都有了大幅度的提升。甚至，如果更进一步要求，我们可以较有把握地评估（误差在 15% 以内）实现列表参数解析需要多长时间。
> 这就是我们认知提升的具体体现。我将这样的工作状态称为“职业程序工作状态”：有序、可控、自信。
> 
> 当然必须说明的是,在参数解析这个例子中，待测系统是命令行参数解析的 Java 类库（Java Library），并不涉及数据库、消息中间件、三方服务等进程外组件，也没有进程内三方组件依赖（比如 OSGi Runtime、Servlet 容器），因而测试上下文相对简单。