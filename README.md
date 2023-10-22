# TestMuseum
看为了让 Junit5 支持自定义类加载器花了多久:[Introduce extension API for providing a custom ClassLoader (e.g., for Powermock)](https://github.com/junit-team/junit5/issues/201)

不过隔壁Spring更是珠玉在前:[Parallel bean initialization during startup [SPR-8767]](https://github.com/spring-projects/spring-framework/issues/13410)

## 项目说明
### 基础依赖
- JDK 17
- gradle 7.3.1
### gradle
#### 配置
gradle wrapper 可以自定义下载的gradle 版本:
`gradle/wrapper/gradle-wrapper.properties`,
gradle会被下载在`~/.gradle/wrapper/dists`
#### 依赖管理
dependencies mirror config:`build.gradle` & `setting.gradle`

发现依赖:  https://package-search.jetbrains.com/

gradle 会在本地缓存:`~/.gradle/caches/modules-2/files-2.1`
## 资料补充
### 测试分类
- 行为验证
- 状态验证
### DDD

## Reference
1.《[徐昊·TDD 项目实战 70 讲](https://time.geekbang.org/column/article/494571)》