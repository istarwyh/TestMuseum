package io.github.istarwyh.util;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.tuple.ImmutablePair;
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

    @Test
    void should_get_pojo_field_name_and_value() {
        io.github.istarwyh.util.ReflectionUtils.setField(whoIAm, "name", xiaohui);
        List<ImmutablePair<String, Object>> fields = ReflectionUtils.getPojoFieldNameAndValue(whoIAm);
        assertEquals(xiaohui, fields.get(0).getValue());
        assertEquals(wuwei, fields.get(1).getValue());
        assertEquals(died, fields.get(2).getValue());
    }

    @Test
    void should_get_pojo_annotation_field_name_and_value() {
        io.github.istarwyh.util.ReflectionUtils.setField(whoIAm, "name", xiaohui);
        List<ImmutablePair<String, Object>> fields =
                ReflectionUtils.getPojoFieldNameAndValue(
                        whoIAm, it -> it.isAnnotationPresent(MyAnnotation.class));
        assertEquals(xiaohui, fields.get(0).getValue());
        assertEquals(1, fields.size());
    }

    @Test
    void get_all_settable_valid_fields() {
        List<Field> allValidFields =
                ReflectionUtils.getAllSettableFields(SampleClass.class);
        assertEquals(4, Objects.requireNonNull(allValidFields).size());
        assertTrue(allValidFields.stream().noneMatch(field -> field.getName().equals("intValue")));
        assertTrue(allValidFields.stream().noneMatch(field -> field.getName().equals("longValue")));
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

    @Data
    static class SampleParentClass {
        private LocalDateTime localDateTimeValue;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    static class SampleClass extends SampleParentClass {
        private static int intValue;
        public static final long longValue = 1L;
        private Integer integerValue = 0;
        private Long longObjectValue;
        private String stringValue;
    }

}
