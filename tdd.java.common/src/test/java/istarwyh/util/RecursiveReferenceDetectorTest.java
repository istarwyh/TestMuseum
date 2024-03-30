package istarwyh.util;

import static istarwyh.junit5.provider.JsonFileArgumentsProviderTest.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

public class RecursiveReferenceDetectorTest {

  @Test
  public void should_judge_recursion() {
    RecursionClass recursionClass = new EasyRandom().nextObject(RecursionClass.class);
    boolean res = RecursiveReferenceDetector.hasRecursiveReference(recursionClass);
    assertTrue(res);
  }

  @Test
  public void should_not_judge_recursion() {
    Object object = new EasyRandom().nextObject(Object.class);
    boolean res = RecursiveReferenceDetector.hasRecursiveReference(object);
    assertFalse(res);
  }
}
