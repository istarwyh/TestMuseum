package io.github.istarwyh.junit5.annotation;

import io.github.istarwyh.junit5.provider.JsonFileArgumentsProvider;
import java.lang.annotation.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * @author xiaohui
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ArgumentsSource(JsonFileArgumentsProvider.class)
@ParameterizedTest
public @interface JsonFileSource {

    /**
     * The JsonFile Resource Path in the test/resources
     */
    String[] resources();

}