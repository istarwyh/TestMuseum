package istarwyh.util;

import static istarwyh.util.ObjectInitUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.alibaba.fastjson2.JSON;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @Disabled
    @Test
    void should_handle_non_builtin_types() {
        Class<?> clazz = SampleClassWithNonBuiltinType.class;
        Object instance = ObjectInitUtil.initWithDefault(clazz);
        assertNotNull(instance);
        assertNotNull(((SampleClassWithNonBuiltinType) instance).getCustomTypeValue());
    }

    @Test
    void should_handle_time_string() {
        ObjectInitUtil.specifyCustomValueGenerator(String.class,null);
        Object instance = ObjectInitUtil.initWithRandom((Class<?>) SampleClassWithFrequentTypes.class);
        assertNotNull(instance);
        String endTime = ((SampleClassWithFrequentTypes) instance).getEndTime();
        assertNotNull(LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @ParameterizedTest
    @CsvSource({"id,true", "no,true", "number,true", "serial,true"})
    void is_about_number(String fieldName, boolean expectedResult) {
        assertEquals(expectedResult, isAboutNumber(fieldName));
    }

    @ParameterizedTest
    @CsvSource({"enum,true", "code,true", "status,true", "type,true"})
    void is_about_enum(String fieldName, boolean expectedResult) {
        assertEquals(expectedResult, isAboutEnum(fieldName));
    }

    @ParameterizedTest
    @CsvSource({"time,true", "date,true", "create,true", "modified,true"})
    void is_about_time(String fieldName, boolean expectedResult) {
        assertEquals(expectedResult, isAboutTime(fieldName));
    }

    @TestFactory
    Stream<DynamicTest> generate_string() {
        Field mockField = mock(Field.class);
        Collection<Object[]> testData = Arrays.asList(new Object[][]{
                {"id", true, "0"},
                {"enum", true, null},
                {"time", true, LocalDateTime.now().getYear()},
                {"other", true, "other"}
        });

        return testData.stream().map(data -> dynamicTest("Test generateString for field: " + data[0], () -> {
            when(mockField.getName()).thenReturn((String) data[0]);
            String result = generateString((Boolean) data[1], mockField.getName());

            if (data[2] == null) {
                assertNull(result);
            } else if(data[2] instanceof Integer){
                assertEquals(data[2],Integer.valueOf(result.substring(0,4)));
            } else {
                assertEquals(data[2], result);
            }
        }));
    }


    @Test
    void should_generate_custom_class_value_with_spi(){
        Class<CustomClassValueGenerator.CustomClass> clazz = CustomClassValueGenerator.CustomClass.class;
        CustomClassValueGenerator.CustomClass customClass = initWithDefault(clazz);
        assertEquals(10, customClass.getValue());
        assertEquals(11, customClass.getCustomClass().getValue());
        assertNull(customClass.getCustomClass().getCustomClass());
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
        private String endTime;
        private List<String> listValue;
        private Set<String> setValue;
        private Map<String, String> mapValue;
        private SampleEnum enumValue;
        private Date dateValue;
    }

    @Data
    static class SampleClassWithNonBuiltinType {
        private CustomType customTypeValue;
        private SampleClassWithNonBuiltinType sampleClassWithNonBuiltinType;
        private List<SampleClassWithNonBuiltinType> simpleClassList;
    }

    enum SampleEnum {
        VALUE1, VALUE2, VALUE3
    }

    @Data
    static class CustomType {
        private String customField;
    }
}
