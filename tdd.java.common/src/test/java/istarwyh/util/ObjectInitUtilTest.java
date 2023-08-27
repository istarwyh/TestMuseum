package istarwyh.util;

import com.alibaba.fastjson2.JSON;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

int
public class ObjectInitUtilTest {


    @DisplayName("input a Class, return the instance of Class")
    @Test
    void init(){
        Class<?> clazz = SampleClass.class;
        SampleClass instance = (SampleClass) ObjectInitUtil.init(clazz);
        assertNotNull(instance);

    }

    @DisplayName("the valid fields of the class was set with default value")
    @Test
    void initClassWithDefault(){
        SampleClass sampleClass = ObjectInitUtil.initWithDefault(SampleClass.class);
        System.out.println(JSON.toJSONString(sampleClass));
        AssertPlus.assertAllNotNull(sampleClass);
    }

    @DisplayName("the valid fields of the instance was set with default value")
    @Test
    void initInstanceWithDefault(){
        SampleClass sampleClass = ObjectInitUtil.initWithDefault(new SampleClass());
        System.out.println(JSON.toJSONString(sampleClass));
        AssertPlus.assertAllNotNull(sampleClass);
    }

    @DisplayName("the valid fields of the instance was set with default value")
    @Test
    void initInstanceWithRandom(){
        SampleClass sampleClass = ObjectInitUtil.initWithRandom(new SampleClass());
        System.out.println(JSON.toJSONString(sampleClass));
        AssertPlus.assertAllNotNull(sampleClass);
    }

    @DisplayName("""
    1. if the field are builtin type, return default or random value
    2. if the field are custom type, return value generator to generate value
    """)
    @Test
    void generateValue(){
        int value = ObjectInitUtil.generateValue(int.class,true);
        assertEquals(0,value);
    }

    @DisplayName("the valid fields of the instance was set with random value")
    @Test
    void initWithRandom(){
        SampleClass sampleClass = ObjectInitUtil.initWithRandom(SampleClass.class);
        System.out.println(JSON.toJSONString(sampleClass));
        AssertPlus.assertAllNotNull(sampleClass);
    }


    @DisplayName("if the fields are not builtin types, allowing user to specify or override" +
            " custom value generator for generating the field value")
    @Test
    void initWithCustomValueGenerator(){
        ObjectInitUtil.specifyCustomValueGenerator(String.class,(any) -> "test");
        CustomType customType = ObjectInitUtil.initWithDefault(CustomType.class);
        assertEquals("test",customType.getCustomField());
    }


    @DisplayName("""
                  settable fields:
                  1. not static && final
                  2. not abstract or interface
                  3. all fields of the instance including the superclass and subclass
            """)
    @Test
    void get_all_settable_valid_fields(){
        List<Field> allValidFields = ObjectInitUtil.getAllSettableFields(SampleClass.class);
        assertEquals(5, Objects.requireNonNull(allValidFields).size());
        assertTrue(allValidFields.stream().anyMatch(field -> field.getName().equals("intValue")));
        assertTrue(allValidFields.stream().noneMatch(field -> field.getName().equals("longValue")));
    }


    @Test
    void should_handle_non_builtin_types() {
        Class<?> clazz = SampleClassWithNonBuiltinType.class;
        Object instance = ObjectInitUtil.initWithRandom(clazz);
        assertNotNull(instance);
        assertNotNull(((SampleClassWithNonBuiltinType) instance).getCustomTypeValue());
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

    @Data
    static class SampleClassWithFrequentTypes {
        private String stringValue;
        private List<String> listValue;
        private Set<String> setValue;
        private Map<String, String> mapValue;
        private SampleEnum enumValue;
        private Date dateValue;
    }

    @Data
    static
    class SampleClassWithNonBuiltinType {
        private CustomType customTypeValue;
    }

    enum SampleEnum {
        VALUE1, VALUE2, VALUE3
    }

    @Data
    static class CustomType {
        private String customField;
    }

}
