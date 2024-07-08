package io.github.istarwyh.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ReflectionUtilsTest {

    private static final String wuwei = "wuwei";
    private static final String died = "died";
    private final String xiaohui = "xiaohui";
    private WhoIAm whoIAm;
    private WhereIGo whereIGo;

    @BeforeEach
    void setUp() {
        whoIAm = new WhoIAm();
        whereIGo = new WhereIGo();
    }

    @Test
    void should_throw_exception_if_setting_static_field() {
        String value = "island";
        assertThrows(
                NoSuchFieldException.class, () -> ReflectionUtils.setField(whoIAm, "country", value));
    }

    @Test
    void should_set_static_field() {
        String value = "island";
        Object oldValue = ReflectionUtils.getField(whoIAm, "country");
        ReflectionUtils.setStaticField(whoIAm, "country", value);
        Object newValue = ReflectionUtils.getField(whoIAm, "country");
        assertEquals(value, newValue);
        ReflectionUtils.setStaticField(whoIAm, "country", oldValue);
    }

    @ParameterizedTest
    @CsvSource(value = {"name,me", "heart,will always go on"})
    void should_set_final_field_and_get_it(String fieldName, String value) {
        ReflectionUtils.setField(whoIAm, fieldName, value);
        String heart = ReflectionUtils.getField(whoIAm, fieldName);
        assertEquals(value, heart);
    }

    @Test
    void should_set_parent_final_field_and_get_it() {
        String value = "will always go on";
        String fieldName = "heart";
        ReflectionUtils.setField(whereIGo, fieldName, value);
        String heart = ReflectionUtils.getField(whereIGo, fieldName);
        assertEquals(value, heart);
    }

    @Test
    void should_get_static_field_value() {
        String country = ReflectionUtils.getField(whereIGo, "country");
        assertEquals(wuwei, country);
    }

    @Test
    void should_not_get_non_exist_field_value_with_null() {
        assertDoesNotThrow(() -> ReflectionUtils.getField(whereIGo, "died"));
    }
   

    public static class WhoIAm {

        @MyAnnotation private String name;

        private static final String country = wuwei;

        private final String heart = died;

        private final TestClassEnum type = TestClassEnum.WHO_AM_I;
    }

    public static class WhereIGo extends WhoIAm {}

    public enum TestClassEnum {
        WHO_AM_I;
    }
}
