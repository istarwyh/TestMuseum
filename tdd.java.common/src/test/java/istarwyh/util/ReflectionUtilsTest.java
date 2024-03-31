package istarwyh.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
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

  @ParameterizedTest
  @CsvSource(value = {"name,me", "heart,will always go on"})
  void should_set_final_field_and_get_it(String fieldName, String value)
      throws NoSuchFieldException {
    ReflectionUtils.setField(whoIAm, fieldName, value);
    String heart = ReflectionUtils.getField(whoIAm, fieldName);
    assertEquals(value, heart);
  }

  @Test
  void should_set_parent_final_field_and_get_it() throws NoSuchFieldException {
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
  void should_not_get_final_field_value() {
    assertDoesNotThrow(() -> ReflectionUtils.getField(whereIGo, "died"));
  }

  @Test
  void should_not_set_final_field_value() {
    assertThrows(
            NoSuchFieldException.class, () -> ReflectionUtils.setField(whereIGo, "died", null));
  }

  @Test
  void should_get_pojo_field_name_and_value() throws NoSuchFieldException {
    ReflectionUtils.setField(whoIAm, "name", xiaohui);
    List<ImmutablePair<String, Object>> fields = ReflectionUtils.getPojoFieldNameAndValue(whoIAm);
    assertEquals(xiaohui, fields.get(0).getValue());
    assertEquals(wuwei, fields.get(1).getValue());
    assertEquals(died, fields.get(2).getValue());
  }

  @Test
  void should_get_pojo_annotation_field_name_and_value() throws NoSuchFieldException {
    ReflectionUtils.setField(whoIAm, "name", xiaohui);
    List<ImmutablePair<String, Object>> fields =
        ReflectionUtils.getPojoFieldNameAndValue(
            whoIAm, it -> it.isAnnotationPresent(MyAnnotation.class));
    assertEquals(xiaohui, fields.get(0).getValue());
    assertEquals(1, fields.size());
  }

  @Test
  void get_all_settable_valid_fields() {
    List<Field> allValidFields =
        ReflectionUtils.getAllSettableFields(ObjectInitUtilTest.SampleClass.class);
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
}
