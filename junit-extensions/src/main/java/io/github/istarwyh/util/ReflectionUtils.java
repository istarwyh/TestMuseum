package io.github.istarwyh.util;

import static io.github.istarwyh.util.UnsafeUtils.unsafe;

import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.misc.Unsafe;

/**
 * This class is changed from WhileBox in PowerMock. The class can set instance field,including
 * final field, not null or static field
 *
 * @author xiaohui
 */
public class ReflectionUtils {

  /**
   * If you want to set static field, you should call {@link ReflectionUtils#setStaticField(Object,
   * String,Object)}
   *
   * @param modifiedObj modifiedObj
   * @param fieldName including final field, not null or static field
   * @param value value
   */
  @SneakyThrows(NoSuchFieldException.class)
  public static void setField(Object modifiedObj, String fieldName, Object value) {
    setField(modifiedObj, fieldName, value, field -> notStaticField(modifiedObj, field));
  }

  @SneakyThrows(NoSuchFieldException.class)
  public static void setStaticField(Object modifiedObj, String fieldName, Object value) {
    setField(modifiedObj, fieldName, value, field -> Modifier.isStatic(field.getModifiers()));
  }

  private static void setField(
      Object modifiedObj, String fieldName, Object value, Predicate<Field> fieldPredicate)
      throws NoSuchFieldException {
    Field foundField =
        findFieldInHierarchy(modifiedObj, fieldName, fieldPredicate)
            .orElseThrow(
                () ->
                    new NoSuchFieldException(
                        String.format(
                            "No %s field named \"%s\" could be found in the \"%s\" class hierarchy",
                            isClass(modifiedObj) ? "static" : "instance",
                            fieldName,
                            getClassOf(modifiedObj).getName())));
    setField(modifiedObj, foundField, value);
  }

  @Nullable
  public static <T> T getField(Object object, String fieldName) {
    return getFieldWithFilter(object, fieldName, anyField -> true);
  }

  @Nullable
  @SneakyThrows({IllegalAccessException.class})
  public static <T> T getFieldWithFilter(
      Object object, String fieldName, Predicate<Field> fieldPredicate) {
    Field foundField = findFieldInHierarchy(object, fieldName, fieldPredicate).orElse(null);
    if (foundField == null) {
      return null;
    }
    return (T) foundField.get(object);
  }

  public static Optional<Field> findFieldInHierarchy(
      Object modifiedObj, String fieldName, @NotNull Predicate<Field> fieldPredicate) {
    if (modifiedObj == null) {
      throw new IllegalArgumentException("The modifiedObj containing the field cannot be null!");
    }
    Class<?> startClass = getClassOf(modifiedObj);

    Optional<Field> optionalField =
        findFieldByUniqueName(fieldName, startClass).filter(fieldPredicate);
    optionalField.ifPresent(it -> it.setAccessible(true));
    return optionalField;
  }

  public static Optional<Field> findFieldByUniqueName(String fieldName, Class<?> startClass) {
    FieldSearchCriteria criteria =
        new FieldSearchCriteria(startClass, field -> field.getName().equals(fieldName), fieldName);
    return findField(criteria);
  }

  private static Optional<Field> findField(FieldSearchCriteria criteria) {
    Field foundField = null;
    Class<?> currentClass = criteria.getStartClass();
    while (currentClass != null) {
      Field[] declaredFields = currentClass.getDeclaredFields();
      for (val field : declaredFields) {
        if (criteria.getMatcher().apply(field)) {
          if (foundField != null) {
            throw new IllegalStateException(
                "Two or more fields matching " + criteria.getErrorMessage() + ".");
          }
          foundField = field;
        }
      }
      if (foundField != null) {
        break;
      }
      currentClass = currentClass.getSuperclass();
    }
    return Optional.ofNullable(foundField);
  }

  @Getter
  @RequiredArgsConstructor
  private static class FieldSearchCriteria {
    private final Class<?> startClass;

    private final Function<Field, Boolean> matcher;

    private final String errorMessage;
  }

  private static boolean notStaticField(Object object, Field field) {
    if (isClass(object)) {
      return Modifier.isStatic(field.getModifiers());
    } else {
      return !Modifier.isStatic(field.getModifiers());
    }
  }

  private static Class<?> getClassOf(@NotNull Object object) {
    Class<?> type;
    if (isClass(object)) {
      type = (Class<?>) object;
    } else {
      type = object.getClass();
    }
    return type;
  }

  private static boolean isClass(Object object) {
    return object instanceof Class<?>;
  }

  public static void setField(Object object, Field foundField, Object value) {
    boolean isStatic = isModifier(foundField, Modifier.STATIC);
    Unsafe unsafe = unsafe();
    if (isStatic) {
      setStaticFieldUsingUnsafe(foundField, value);
    } else {
      setFieldUsingUnsafe(object, foundField, unsafe.objectFieldOffset(foundField), value);
    }
  }

  private static void setStaticFieldUsingUnsafe(Field field, Object value) {
    Object base = unsafe().staticFieldBase(field);
    long offset = unsafe().staticFieldOffset(field);
    setFieldUsingUnsafe(base, field, offset, value);
  }

  /**
   * judge whether modifier the field belongs to
   *
   * @param field field
   * @param modifier {@link Modifier#STATIC }.etc
   * @return if modifier the field belongs to
   */
  private static boolean isModifier(Field field, int modifier) {
    return (field.getModifiers() & modifier) == modifier;
  }

  @SneakyThrows
  @SuppressWarnings({"all", "removal"})
  private static void setFieldUsingUnsafe(Object base, Field field, long offset, Object newValue) {
    field.setAccessible(true);
    boolean isFinal = isModifier(field, Modifier.FINAL);
    if (isFinal) {
      AccessController.doPrivileged(
          (PrivilegedAction<Object>)
              () -> {
                setFieldUsingUnsafe(base, field.getType(), offset, newValue);
                return null;
              });
    } else {
      field.set(base, newValue);
    }
  }

  /**
   * 使用 Unsafe 类提供的底层方法直接设置对象的字段值,绕过了 Java 语言的访问控制和安全检查。
   *
   * @param base 要修改字段的对象
   * @param type 字段的类型,使用基本类型的包装类表示
   * @param offset 段在对象内存中的偏移量
   * @param newValue 要设置的新值
   */
  private static void setFieldUsingUnsafe(
      Object base, Class<?> type, long offset, Object newValue) {
    if (type == Integer.TYPE) {
      unsafe().putInt(base, offset, (Integer) newValue);
    } else if (type == Short.TYPE) {
      unsafe().putShort(base, offset, (Short) newValue);
    } else if (type == Long.TYPE) {
      unsafe().putLong(base, offset, (Long) newValue);
    } else if (type == Byte.TYPE) {
      unsafe().putByte(base, offset, (Byte) newValue);
    } else if (type == Boolean.TYPE) {
      unsafe().putBoolean(base, offset, (Boolean) newValue);
    } else if (type == Float.TYPE) {
      unsafe().putFloat(base, offset, (Float) newValue);
    } else if (type == Double.TYPE) {
      unsafe().putDouble(base, offset, (Double) newValue);
    } else if (type == Character.TYPE) {
      unsafe().putChar(base, offset, (Character) newValue);
    } else {
      unsafe().putObject(base, offset, newValue);
    }
  }

  @NotNull
  @SuppressWarnings("unchecked")
  private static <T> Class<T> getClassFromParameterizedType(Type actualTypeArgument) {
    if (actualTypeArgument instanceof Class) {
      return (Class<T>) actualTypeArgument;
    } else {
      return getClassFromParameterizedType(((ParameterizedType) actualTypeArgument).getRawType());
    }
  }

  public static <POJO> List<ImmutablePair<String, Object>> getPojoFieldNameAndValue(POJO pojo) {
    return getPojoFieldNameAndValue(pojo, any -> true);
  }

  public static <POJO> List<ImmutablePair<String, Object>> getPojoFieldNameAndValue(
      POJO pojo, Predicate<Field> fieldPredicate) {
    List<Field> fields =
        Arrays.stream(pojo.getClass().getDeclaredFields())
            .filter(fieldPredicate)
            .peek(field -> field.setAccessible(true))
            .collect(Collectors.toList());
    return fields.stream()
        .map(
            field -> {
              try {
                Object value = field.get(pojo);
                return new ImmutablePair<>(field.getName(), value);
              } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
              }
            })
        .collect(Collectors.toList());
  }

  /**
   * find the first generic clazz of the given interface
   *
   * @param originInterface the generic interface
   * @param concreteClass the concrete class the implemented the generic interface
   * @return the generic interface generic clazz
   * @param <T> the generic interface generic param,like `Interface Example<T>`
   */
  public static <T> Class<T> getInterfaceFirstGenericClazz(
      Class<?> originInterface, Class<?> concreteClass) {
    for (Type type : concreteClass.getGenericInterfaces()) {
      if (type instanceof ParameterizedType
          && ((ParameterizedType) type)
              .getRawType()
              .getTypeName()
              .equals(originInterface.getName())) {
        Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
        if (actualTypeArgument != null) {
          return getClassFromParameterizedType(actualTypeArgument);
        }
      }
    }
    throw new IllegalArgumentException("Invalid interface");
  }

  /**
   * parseGenericTypesFromSignature
   *
   * @param str method signature like
   *     "(Listarwyh/junit5/provider/model/TestCase<Ljava/lang/String;Ljava/lang/String;>;)V"
   * @return (Class)
   */
  @SneakyThrows(ClassNotFoundException.class)
  public static Pair<Class<?>, Class<?>> parseGenericTypesFromSignature(String str) {
    int firstIndex = str.indexOf("<L") + 2;
    int lastIndex = str.lastIndexOf(">;");
    String[] classes = str.substring(firstIndex, lastIndex).split(";L");
    Class<?> firstClass = Class.forName(classes[0].replace('/', '.'));
    Class<?> secondClass = Class.forName(classes[1].replace('/', '.'));
    return Pair.of(firstClass, secondClass);
  }

  /**
   * We added an explicit type parameter Class<?> to the iterate method. This resolves the nested
   * wildcard types.
   *
   * @param clazz any type parameter
   * @return the valid fields of the class
   */
  public static List<Field> getAllSettableFields(Class<?> clazz) {
    List<Field> fields = new ArrayList<>();
    Class<?> currentClass = clazz;
    while (currentClass != null) {
      fields.addAll(
          Arrays.stream(currentClass.getDeclaredFields())
              .filter(field -> !Modifier.isStatic(field.getModifiers()))
              .filter(field -> !Modifier.isFinal(field.getModifiers()))
              .filter(field -> !Modifier.isAbstract(field.getModifiers()))
              .filter(field -> !Modifier.isNative(field.getModifiers()))
              .filter(field -> !Modifier.isTransient(field.getModifiers()))
              .filter(field -> !field.getType().isInterface())
              .collect(Collectors.toList()));
      currentClass = currentClass.getSuperclass();
    }
    return fields;
  }
}
