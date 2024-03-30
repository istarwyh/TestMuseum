package istarwyh.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

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
    return !Stream.of(obj).allMatch(newObj -> checkAndAdd(visited, newObj));
  }

  private static boolean checkAndAdd(IdentityHashMap<Object, Boolean> visited, Object obj) {
    return Optional.of(obj.getClass())
        .filter(it -> !TypeUtil.isBuiltInType(it))
        .map(
            it ->
                canIterate(it)
                    // This snippet code should be deleted due to it is unused
                    ? Arrays.stream(toInstanceArray(obj))
                        .map(instance -> instance.getClass().getDeclaredFields())
                        .flatMap(Arrays::stream)
                    : Stream.of(it.getDeclaredFields()))
        .map(
            fieldStream ->
                getFieldValueStream(obj, fieldStream)
                    .allMatch(
                        childObj -> {
                          if (isNotRecursion(visited, childObj)) {
                            return checkAndAdd(visited, childObj);
                          }
                          return false;
                        }))
        .orElse(true);
  }

  private static boolean canIterate(Class<?> it) {
    return it.isArray() || Collection.class.isAssignableFrom(it);
  }

  @NotNull
  private static Stream<Object> getFieldValueStream(Object obj, Stream<Field> fieldStream) {
    return canIterate(obj.getClass())
        ? Arrays.stream(toInstanceArray(obj))
        : fieldStream
            .filter(field -> !TypeUtil.isBuiltInType(field.getType()))
            .filter(field -> !Modifier.isFinal(field.getModifiers()))
            .filter(field -> !Modifier.isAbstract(field.getModifiers()))
            .filter(field -> !Modifier.isNative(field.getModifiers()))
            .filter(field -> !Modifier.isTransient(field.getModifiers()))
            .filter(field -> !Modifier.isInterface(field.getModifiers()))
            .peek(it -> it.setAccessible(true))
            .map(
                it -> {
                  try {
                    System.out.println(it);
                    return it.get(obj);
                  } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                  }
                });
  }

  /**
   * The `putIfAbsent` method returns the value previously associated with the key, or `null` if
   * there was no previous association. If it's the first access, the condition is true,which means
   * no recursive reference is found; if it's not the first access, the condition is false,
   * indicating that a recursive reference has been detected.
   */
  private static boolean isNotRecursion(IdentityHashMap<Object, Boolean> visited, Object childObj) {
    return visited.putIfAbsent(childObj, Boolean.TRUE) == null;
  }

  private static Object[] toInstanceArray(Object array) {
    array = array.getClass().isArray() ? array : ((Collection<?>) array).toArray();
    Object finalArray = array;
    return Stream.iterate(0, i -> i < Array.getLength(finalArray), i -> i + 1)
        .map(i -> Array.get(finalArray, i))
        .toArray();
  }
}
