package istarwyh.util;

import static istarwyh.junit5.provider.JsonFileArgumentsProviderTest.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

public class RecursiveReferenceDetectorTest {

  private final RecursionClass recursionClass = new EasyRandom().nextObject(RecursionClass.class);

  @Test
  public void should_judge_recursion() {
    boolean res = RecursiveReferenceDetector.hasRecursiveReference(recursionClass);
    assertTrue(res);
  }

  @Test
  public void should_judge_recursion_list() {
    List<RecursionClass> recursionClasses = new ArrayList<>();
    recursionClasses.add(recursionClass);
    boolean res = RecursiveReferenceDetector.hasRecursiveReference(recursionClasses);
    assertTrue(res);
  }

  @Test
  public void should_not_judge_recursion() {
    Object object = new EasyRandom().nextObject(Object.class);
    boolean res = RecursiveReferenceDetector.hasRecursiveReference(object);
    assertFalse(res);
  }
}
