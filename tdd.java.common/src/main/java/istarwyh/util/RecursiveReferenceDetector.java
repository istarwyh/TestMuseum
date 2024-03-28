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
   * 这个putIfAbsent 方法会返回该键之前关联的值，如果该键没有之前的关联，则返回null
   * 如果是第一次访问，那么条件为真，这意味着没有发现递归引用；如果不是第一次访问，那么条件为假，这意味着发现了递归引用。
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
