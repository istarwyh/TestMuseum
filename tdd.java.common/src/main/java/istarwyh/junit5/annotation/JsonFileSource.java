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

    /**
     * The Class of Current Annotation {@link JsonFileSource}
     */
    Class<?> of() default Object.class;

    /**
     * The JsonFile Resource Path in the test/resources
     */
    String[] resources();

    /**
     * The parameter class type that jsonFile matches
     */
    Class<?> type() default TestCase.class;

}