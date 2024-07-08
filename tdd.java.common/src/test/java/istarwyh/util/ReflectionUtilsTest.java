package istarwyh.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
  void should_get_pojo_field_name_and_value() {
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
