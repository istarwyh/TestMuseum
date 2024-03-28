package istarwyh.util;

import static istarwyh.junit5.provider.JsonFileArgumentsProviderTest.*;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RecursiveReferenceDetectorTest {

  @Test
  public void hasRecursiveReferenceTest() {
    RecursionClass recursionClass = new EasyRandom().nextObject(RecursionClass.class);
    boolean res = RecursiveReferenceDetector.hasRecursiveReference(recursionClass);
    Assertions.assertTrue(res);
  }
}
