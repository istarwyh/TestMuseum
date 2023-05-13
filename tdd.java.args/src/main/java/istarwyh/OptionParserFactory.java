package istarwyh;

import istarwyh.exceptions.IllegalValueException;
import istarwyh.exceptions.InsufficientArgumentsException;
import istarwyh.exceptions.TooManyArgumentsException;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

/**
 * @Description: OptionParserFactory
 * @Author: wx:istarwyh
 * @Date: 2022-05-22 22:41
 * @Version: ing
 */
public class OptionParserFactory {

    public static OptionParser<Boolean> bool() {
        return (arguments, option) -> values(arguments, option, 0).isPresent();
    }

    public static <T> OptionParser<T> unary(Function<String, T> valueParser, T defaultValue) {
        return (arguments, option) -> values(arguments, option, 1)
                .map(values -> parseValue(valueParser, option, values.get(0)))
                .orElse(defaultValue);
    }


    public static <T> OptionParser<T[]> list(Function<String, T> valueParser, IntFunction<T[]> generator) {
        return (arguments, option) -> values(arguments,option)
                .map(it -> it.stream()
                        .map(value -> parseValue(valueParser,option,value))
                        .toArray(generator))
                .orElse(generator.apply(0));
    }

    private static <T> T parseValue(Function<String, T> valueParser, Option option, String value) {
        try{
            return valueParser.apply(value);
        }catch (Exception ex){
            throw new IllegalValueException(option.value(), value, ex);
        }
    }

    private static Optional<List<String>> values(List<String> arguments, Option option) {
        int index = arguments.indexOf(Args.DASH_PREFIX + option.value());
        return Optional.ofNullable(index == -1 ? null : values(arguments,index));
    }

    private static Optional<List<String>> values(List<String> arguments, Option option, int expectedSize) {
        return values(arguments,option).map( it -> checkSize(option,expectedSize,it));
    }

    private static List<String> checkSize(Option option, int expectedSize, List<String> values) {
        if(values.size() < expectedSize){
            throw new InsufficientArgumentsException(option.value());
        }
        if(values.size() > expectedSize){
            throw new TooManyArgumentsException(option.value());
        }
        return values;
    }

    private static List<String> values(List<String> arguments, int index) {
        final int followingFlag = IntStream.range(index + 1, arguments.size())
                .filter(it -> arguments.get(it).matches("^-[a-zA-Z]+$"))
                .findFirst()
                .orElse(arguments.size());
        return arguments.subList(index + 1, followingFlag);
    }

}
