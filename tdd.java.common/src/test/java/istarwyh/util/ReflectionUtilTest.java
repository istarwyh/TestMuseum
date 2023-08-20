package istarwyh.util;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionUtilTest {

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
        assertThrows(NoSuchFieldException.class,
                () -> ReflectionUtil.setField(whoIAm,"country", value)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"name,me", "heart,will always go on"})
    void should_set_final_field_and_get_it(String fieldName,String value) throws NoSuchFieldException {
        ReflectionUtil.setField(whoIAm,fieldName,value);
        String heart = ReflectionUtil.getField(whoIAm, fieldName);
        assertEquals(value,heart);
    }

    @Test
    void should_set_parent_final_field_and_get_it() throws NoSuchFieldException {
        String value = "will always go on";
        String fieldName = "heart";
        ReflectionUtil.setField(whereIGo, fieldName,value);
        String heart = ReflectionUtil.getField(whereIGo, fieldName);
        assertEquals(value,heart);
    }

    @Test
    void should_get_static_field_value(){
        String country = ReflectionUtil.getField(whereIGo, "country");
        assertEquals(wuwei,country);
    }

    @Test
    void should_get_pojo_field_name_and_value() throws NoSuchFieldException {
        ReflectionUtil.setField(whoIAm,"name", xiaohui);
        List<ImmutablePair<String,Object>> fields = ReflectionUtil.getPojoFieldNameAndValue(whoIAm);
        assertEquals(xiaohui,fields.get(0).getValue());
        assertEquals(wuwei,fields.get(1).getValue());
        assertEquals(died,fields.get(2).getValue());
    }

    @Test
    void should_get_pojo_annotation_field_name_and_value() throws NoSuchFieldException {
        ReflectionUtil.setField(whoIAm,"name", xiaohui);
        List<ImmutablePair<String,Object>> fields = ReflectionUtil.
                getPojoFieldNameAndValue(whoIAm, it -> it.isAnnotationPresent(MyAnnotation.class));
        assertEquals(xiaohui,fields.get(0).getValue());
        assertEquals(1,fields.size());
    }




    public static class WhoIAm{

        @MyAnnotation
        private String name;

        private static final String country = wuwei;

        private final String heart = died;
    }

    public static class WhereIGo extends WhoIAm{}
}