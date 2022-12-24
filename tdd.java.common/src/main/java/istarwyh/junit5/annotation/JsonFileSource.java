package istarwyh.junit5.annotation;

import istarwyh.junit5.provider.JsonFileArgumentsProvider;
import istarwyh.junit5.provider.model.TestCase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ArgumentsSource(JsonFileArgumentsProvider.class)
@ParameterizedTest
public @interface JsonFileSource {

    String[] resources();

    Class<?> type() default TestCase.class;
}