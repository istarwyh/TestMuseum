`juint-extensions` 扩展 junit5-extension，解决 JSON [参数化测试]()和任意测试类加载等问题。基本解决的问题如下
## 怎么无限制地获得一个对象实例？
在测试过程中，我们在Mock对象或者走通全链路逻辑时往往会碰到一些对象难以实例化，从而报各种各样的错误。我个人碰到的就有：
- 对象没有public构造函数，无法实例化
- 公司内部类加载器不允许直接通过AppClassLoader实例化
- 对象实例化过程中会去调用Native方法，本地环境不合适

想要解决这些问题，有如下办法：
### 利用Unsafe直接分配内存
```java
<T> T allocateInstance(Class<T> type) {
    Field unsafe = Unsafe.class.getDeclaredField("theUnsafe");
    unsafe.setAccessible(true);
    Unsafe u = (Unsafe)unsafe.get(Unsafe.class);
    return (T) u.allocateInstance(type);
}
```

但是如果类加载过程中就会有报错如下,Unsafe也是没办法的:
```java
public class CannotInitialDueToLoadingClassError {
    public static final Integer id;

    static {
        id = 1 / 0;
    }
}
```
### 修改加载时字节码
如果可以直接替换类加载器，从而可以在类加载的过程中修改其字节码，那么就无论什么类都可以被实例化了。通过这样的方法，实际上也可以对类做任意的动作了。
比如通过 `io.github.istarwyh.classloader.modifier.Modifier` 来实现`CannotInitialDueToLoadingClassError` 的实例化：
```java
public class CannotInitialByLoadingClassErrorModifier implements Modifier {

    @Override
    @SneakyThrows
    public void afterLoadClass(String className, CtClass ctClass) {
        Class<?> startClass = CannotInitialDueToLoadingClassError.class;
        while (startClass != Object.class){
            if(startClass.getName().equals(className)){
                wipeOriginalByteCode(ctClass);
            }
            startClass = startClass.getSuperclass();
        }
    }
}
```
## 怎么样无限制的设置对象值？
在 Java 中,有些特殊的字段是无法通过反射直接修改的, 比如`static final` 字段。
被 static final 修饰的字段表示静态常量,其值在类加载时就已经确定,并且在整个程序运行期间不能被修改，因为 static final 字段在类加载时已经被替换为常量值,不允许再次修改。。例如:
```java
public class MyClass {
public static final int VALUE = 42;

    public static int getValue() {
        return VALUE;
    }
}
```

然而,在某些测试场景下,我们可能需要修改这些特殊字段的值,以便于对代码进行隔离测试。 使用 Unsafe 类提供的 putXXX 方法,如 putInt、putObject 等,可以直接修改对象内存中的字段值,而无需经过 Java 语言层面的检查和限制。
这样即使是 static final 字段,也可以被修改。以及我们还可以设置父类的任意字段。具体操作如下：
```java
@Test
void should_set_static_field() {
    String value = "island";
    Object oldValue = ReflectionUtils.getField(whoIAm, "country");
    ReflectionUtils.setStaticField(whoIAm, "country", value);
    Object newValue = ReflectionUtils.getField(whoIAm, "country");
    assertEquals(value, newValue);
    ReflectionUtils.setStaticField(whoIAm, "country", oldValue);
}

@Test
void should_set_parent_final_field_and_get_it() {
    String value = "will always go on";
    String fieldName = "heart";
    ReflectionUtils.setField(whereIGo, fieldName, value);
    String heart = ReflectionUtils.getField(whereIGo, fieldName);
    assertEquals(value, heart);
}
```

## 自动生成JSON参数的参数化测试
只需要一个 `@JsonFileSource` 注解，再随便指定文件的名字即可自动基于方法签名中的参数名字产生对象实例，而这个实例可以在测试中访问到，
从而起到测试数据和测试逻辑分离的设计效果。 
```java
@JsonFileSource(resources = {"absent_test_case.json"})
void should_generate_test_case_json_lack_of_it(TestCase<String, String> testCase) {
    assertEquals("eOMtThyhVNLWUZNRcBaQKxI", testCase.getInput());
    assertEquals("yedUsFwdkelQbxeTeQOvaScfqIOOmaa", testCase.getOutput());
}
```