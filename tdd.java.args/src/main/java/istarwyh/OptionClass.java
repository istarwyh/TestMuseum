package istarwyh;


import istarwyh.exceptions.IllegalAnnotationException;
import istarwyh.exceptions.UnsupportedOptionTypeException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public record OptionClass<T>(Class<T> clazz, Map<Class<?>, OptionParser<?>> parsers) {

    @SuppressWarnings("unchecked")
    public T parse(String[] args) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        List<String> arguments = Arrays.asList(args);
        Constructor<?> constructor = this.clazz.getDeclaredConstructors()[0];

        final Object[] values = Arrays.stream(constructor.getParameters())
                .map(it -> parseOption(arguments, it))
                .toArray();

        Object instance = constructor.newInstance(values);
        return (T) instance;
    }

    private Object parseOption(List<String> arguments, Parameter it) {
        if (!it.isAnnotationPresent(Option.class)) {
            throw new IllegalAnnotationException(it.getName());
        }
        Option option = it.getAnnotation(Option.class);
        final Class<?> type = it.getType();
        if (!parsers.containsKey(type)) {
            throw new UnsupportedOptionTypeException(option.value(), type);
        }
        return parsers.get(type).parse(arguments, option);
    }
}
