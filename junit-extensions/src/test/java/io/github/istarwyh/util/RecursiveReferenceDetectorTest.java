package io.github.istarwyh.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.istarwyh.junit5.provider.JsonFileArgumentsProviderTest;
import io.github.istarwyh.junit5.provider.JsonFileArgumentsProviderTest.RecursionClass;
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

  @Test
  public void should_not_judge_recursion_2() {
    Object object = new EasyRandom().nextObject(JsonFileArgumentsProviderTest.People.class);
    boolean res = RecursiveReferenceDetector.hasRecursiveReference(object);
    assertFalse(res);
  }
}
