package istarwyh.util;

import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.Test;

import static istarwyh.util.ReflectionUtilTest.WhoIAm;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AssertPlusTest {


    @Test
    void should_compare_two_object_just_with_their_field_value() throws NoSuchFieldException {
        WhoIAm lele = new WhoIAm();
        WhoIAm dudu = new WhoIAm();
        ReflectionUtil.setField(lele,"name","me");
        ReflectionUtil.setField(dudu,"name","me");
        assertDoesNotThrow(() -> AssertPlus.compareFields(lele,dudu));
        ReflectionUtil.setField(lele,"name","le");
        ReflectionUtil.setField(dudu,"name","du");
        assertThrows(Throwable.class,() -> AssertPlus.compareFields(lele,dudu));
    }

    @Test
    void compare_json_without_default_order_being_true_and_with_order_being_false(){
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("key1","value1");
        jsonObject1.put("key2","value2");
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("key2","value2");
        jsonObject2.put("key1","value1");
        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("key3","value3");
        jsonObject1.put("key3",jsonObject3);
        jsonObject2.put("key3",jsonObject3);
        assertDoesNotThrow(() -> AssertPlus.compareFields(jsonObject1, jsonObject2));
        assertThrows(Throwable.class,
                () -> AssertPlus.compareFieldsWithOrder(jsonObject1, jsonObject2)
        );
    }
}