package istarwyh.util;

import java.lang.reflect.Array;
import java.util.IdentityHashMap;
import java.util.stream.Stream;

/**
 * @author mac
 */
public class RecursiveReferenceDetector {

  /**
   * Checks for recursive references in an object using a breadth-first search approach.
   *
   * @param obj The object to check.
   * @return true if a recursive reference is found, false otherwise.
   */
  public static boolean hasRecursiveReference(Object obj) {
    IdentityHashMap<Object, Boolean> visited = new IdentityHashMap<>();
    return !Stream.of(obj)
        .filter(o -> !TypeUtil.isBuiltInType(o.getClass()))
        .allMatch(newObj -> checkAndAdd(visited, newObj));
  }

  private static boolean checkAndAdd(IdentityHashMap<Object, Boolean> visited, Object obj) {
    return ReflectionUtil.getDeclaredFields(obj)
        .flatMap(
            objArray ->
                objArray != null && objArray.getClass().isArray()
                    ? Stream.of(toObjectArray(objArray))
                    : Stream.of(objArray))
        .allMatch(
            childObj ->
                childObj == null
                    || TypeUtil.isBuiltInType(childObj.getClass())
                    || isNotRecursion(visited, childObj) && checkAndAdd(visited, childObj));
  }

  /**
   * The `putIfAbsent` method returns the value previously associated with the key,
   * or `null` if there was no previous association.
   * If it's the first access, the condition is true,which means no recursive reference is found;
   * if it's not the first access,
   * the condition is false, indicating that a recursive reference has been detected.
   */
  private static boolean isNotRecursion(IdentityHashMap<Object, Boolean> visited, Object childObj) {
    return visited.putIfAbsent(childObj, Boolean.TRUE) == null;
  }

  private static Object[] toObjectArray(Object array) {
    return Stream.iterate(0, i -> i < Array.getLength(array), i -> i + 1)
        .map(i -> Array.get(array, i))
        .toArray();
  }
}
