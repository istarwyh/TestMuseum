package istarwyh;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

import static istarwyh.OptionParserFactory.*;


/**
 * @Description: tdd.args.Args
 * @Author: wx:istarwyh
 * @Date: 2022-05-15 20:32
 * @Version: ing
 */
@Slf4j
public class Args {

    public static final String DASH_PREFIX = "-";

    @SneakyThrows
    public static <T> T parse(Class<T> clazz, String... args) {
        log.info("clazz:{},args:{}",clazz.getName(), Arrays.stream(args).toArray());
        return new OptionClass<T>(clazz, PARSERS).parse(args);
    }

    private static final Map<Class<?>,OptionParser<?>> PARSERS = Map.of(
                    boolean.class, bool(),
                    int.class, unary(Integer::parseInt, 0),
                    String.class, unary(Function.identity(), ""),
                    String[].class,list(String::valueOf,String[]::new),
                    Integer[].class,list(Integer::parseInt,Integer[]::new)
            );

}
