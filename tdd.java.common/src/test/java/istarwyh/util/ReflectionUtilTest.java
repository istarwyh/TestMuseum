package istarwyh.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionUtilTest {

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
        assertEquals("wuwei",country);
    }


    public static class WhoIAm{

        private String name;

        private static String country = "wuwei";

        private final String heart = "died";
    }

    public static class WhereIGo extends WhoIAm{}
}