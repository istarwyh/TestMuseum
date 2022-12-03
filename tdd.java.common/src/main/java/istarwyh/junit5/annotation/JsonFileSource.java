package istarwyh.junit5.annotation;

import com.alibaba.fastjson2.TypeReference;
import istarwyh.provider.JsonFileArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;
import java.util.List;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ArgumentsSource(JsonFileArgumentsProvider.class)
public @interface JsonFileSource {

    String[] resources();

    Class<?> type() default String.class;
}