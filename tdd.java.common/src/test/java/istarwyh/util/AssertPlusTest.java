package istarwyh.util;

import com.alibaba.fastjson2.JSONObject;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static istarwyh.util.ReflectionUtilsTest.WhereIGo;
import static istarwyh.util.ReflectionUtilsTest.WhoIAm;
import static org.junit.jupiter.api.Assertions.*;

class AssertPlusTest {

  private final EasyRandom easyRandom = new EasyRandom();

  @Test
  void should_compare_two_objects_and_find_different_field_values() throws NoSuchFieldException {
    WhoIAm lele = new WhoIAm();
    WhoIAm dudu = new WhoIAm();
    ReflectionUtils.setField(lele, "name", "le");
    ReflectionUtils.setField(dudu, "name", "du");
    Throwable throwable =
        AssertPlus.assertThrows(Throwable.class, AssertPlus::compareFields, lele, dudu);
    System.out.println(throwable.toString());
  }

  @Test
  void should_compare_two_object_just_with_their_field_value() {
    WhereIGo lele = easyRandom.nextObject(WhereIGo.class);
    WhereIGo dudu = easyRandom.nextObject(WhereIGo.class);
    assertThrows(AssertionFailedError.class, () -> AssertPlus.compareFields(lele, dudu));
  }

  @Test
  void compare_json_without_default_order_being_true_and_with_order_being_false() {
    JSONObject jsonObject1 = new JSONObject();
    jsonObject1.put("key1", "value1");
    jsonObject1.put("key2", "value2");
    JSONObject jsonObject2 = new JSONObject();
    jsonObject2.put("key2", "value2");
    jsonObject2.put("key1", "value1");
    JSONObject jsonObject3 = new JSONObject();
    jsonObject3.put("key3", "value3");
    jsonObject1.put("key3", jsonObject3);
    jsonObject2.put("key3", jsonObject3);
    assertDoesNotThrow(() -> AssertPlus.compareFields(jsonObject1, jsonObject2));
    Throwable throwable =
        AssertPlus.assertThrows(
            Throwable.class, AssertPlus::compareFieldsWithOrder, jsonObject1, jsonObject2);
    System.out.println(throwable.toString());
  }

  @DisplayName("assert all fields including in superclass cannot be null")
  @Test
  void assertAllFieldsNotNull() {
    assertDoesNotThrow(() -> AssertPlus.assertAllNotNull(new Object()));
  }
}
