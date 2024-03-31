package istarwyh.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import istarwyh.junit5.annotation.JsonFileSource;
import istarwyh.junit5.provider.model.TestCase;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FunctionCallTest {

    private static Map<String, Function<Args, JSON>> methodMap;
    private static Map<String, Method> methodMap2;

    @BeforeEach
    void setUp() {
        methodMap = new HashMap<>();
//        methodMap.put("function1", this::function1);
//        this.getClass().getMethod("");
    }

    @Disabled
    @JsonFileSource(resources = "resources.json")
    void test(TestCase<List<BaseFunctionParam>,Void> testCase) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<BaseFunctionParam> input = testCase.getInput(new TypeReference<>() {
        });
        BaseFunctionParam baseFunctionParam0 = input.get(0);

        Args args = baseFunctionParam0.getArgs();
        String function = baseFunctionParam0.getName();
        JSON result = methodMap.get(function).apply(args);

        List<Object> argsDetailParam = parseDetailParam(args);
        Parameter[] parameters = this.getClass().getDeclaredMethod("function1").getParameters();
        List<Object> sortedArgsDetailParams = new ArrayList<Object>();
        Map<String, ? extends Class<?>> argsDetailName2ArgsDetail = argsDetailParam.stream()
                .collect(Collectors.toMap(it -> it.getClass().getName(), Object::getClass, (k1, k2) -> k1));
        for(int i = 0; i < parameters.length; i++) {
            String argsDetailName = parameters[i].getName();
            sortedArgsDetailParams.set(i,argsDetailName2ArgsDetail.get(argsDetailName));
        }
//        argsDetailParam.stream()
//                .sorted(Comparator.comparing(parameters));
//

        JSON result2 = (JSON) methodMap2.get(function).invoke(this,sortedArgsDetailParams);

        System.out.println(JSON.toJSONString(result));
    }

    private List<Object> parseDetailParam(Args args) {

        return null;
    }

    public static class Args{

    }

    @Data
    public static class MyInput{
        Function1Arg2 function1Arg2;

        Function2dddd function2;

    }

    @Data
    public static class BaseFunctionParam{

        String name;

        Args args;
    }
    @Data
    public static class Function1Arg2 {
        private int lalaInt;
        private String lolString;
    }

    @Data
    public static class Function2dddd {
        private String dddd;
    }

    public JSON function1(String arg1, Function1Arg2 arg2) {
        return null;
    }

    public JSON function11(String name, String id) {
        return null;
    }

    public static JSON function2(Function2dddd arg1) {
        return null;
    }

}
