package istarwyh.util;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xiaohui
 */
public class ObjectInitUtil {


    private static final Random RANDOM = new Random();


    private static final ThreadLocal<Map<Class<?>, ValueGenerator<?>>> VALUE_GENERATORS =
            ThreadLocal.withInitial(ObjectInitUtil::initValueGenerators);


    @NotNull
    private static Map<Class<?>, ValueGenerator<?>> initValueGenerators() {
        Map<Class<?>, ValueGenerator<?>> map = new HashMap<>(10);
        map.put(List.class, ObjectInitUtil::generateList);
        map.put(ArrayList.class, ObjectInitUtil::generateList);
        map.put(Set.class, (any) -> new HashSet<>());
        map.put(HashSet.class, (any) -> new HashSet<>());
        map.put(Map.class, ObjectInitUtil::generateMap);
        map.put(HashMap.class, ObjectInitUtil::generateMap);
        map.put(ConcurrentMap.class, (any) -> new ConcurrentHashMap<>(2));
        map.put(ConcurrentHashMap.class, (any) -> new ConcurrentHashMap<>(2));
        map.put(LocalDate.class, ObjectInitUtil::generateLocalDate);
        map.put(LocalDateTime.class, ObjectInitUtil::generateLocalDateTime);
        map.put(Date.class, (any) -> new Date());
        return map;
    }

    @NotNull
    private static String generateString(Field field, boolean useDefaultValue) {
        String name = field.getName();
        if (name.contains("time") ||
                name.contains("Time") ||
                name.contains("date") ||
                name.contains("Date")
        ) {
            return generateLocalDateTime(useDefaultValue).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return String.valueOf(generateValue(int.class, useDefaultValue));
    }

    public static List<Object> generateList(boolean useDefaultValues) {
        int size = useDefaultValues ? 1 : RANDOM.nextInt(10);
        List<Object> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(null);
        }
        return list;
    }

    public static Map<Object, Object> generateMap(boolean useDefaultValues) {
        int size = useDefaultValues ? 1 : RANDOM.nextInt(10);
        Map<Object, Object> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            map.put(null, null);
        }
        return map;
    }

    public static LocalDate generateLocalDate(boolean useDefaultValues) {
        if (useDefaultValues) {
            return LocalDate.now();
        } else {
            long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
            long maxDay = LocalDate.of(2030, 12, 31).toEpochDay();
            long randomDay = minDay + RANDOM.nextInt((int) (maxDay - minDay));
            return LocalDate.ofEpochDay(randomDay);
        }
    }

    public static LocalDateTime generateLocalDateTime(boolean useDefaultValues) {
        if (useDefaultValues) {
            return LocalDateTime.now();
        } else {
            long minDay = LocalDateTime.of(1970, 1, 1, 1, 1, 1)
                    .toEpochSecond(ZoneOffset.UTC);
            long maxDay = LocalDateTime.of(2030, 12, 31, 1, 1, 1)
                    .toEpochSecond(ZoneOffset.UTC);
            long randomDay = minDay + RANDOM.nextInt((int) (maxDay - minDay));
            return LocalDateTime.ofEpochSecond(randomDay, 0, ZoneOffset.UTC);
        }
    }

    public static <T> T initWithDefault(Class<T> clazz) {
        return initWithValues(clazz, true);
    }

    public static <T> T initWithDefault(T instance) {
        return initWithValues(instance, true);
    }

    public static <T> T initWithRandom(T instance) {
        return initWithValues(instance, true);
    }


    private static <T> T initWithValues(Class<T> clazz, boolean useDefaultValue) {
        return initWithValues(init(clazz), useDefaultValue);
    }

    private static <T> T initWithValues(T instance, boolean useDefaultValue) {
        List<Field> allSettableFields = getAllSettableFields(instance.getClass());
        for (Field field : allSettableFields) {
            field.setAccessible(true);
            try {
                Object value = generateValue(field, useDefaultValue);
                field.set(instance, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error accessing field: " + field.getName());
            }
        }
        return instance;
    }

    private static Object generateValue(Field field, boolean useDefaultValue) {
        Class<?> fieldType = field.getType();
        ValueGenerator<?> valueGenerator = VALUE_GENERATORS.get().get(fieldType);
        if (valueGenerator != null) {
            return valueGenerator.generateValue(useDefaultValue);
        } else if (fieldType == String.class) {
            return generateString(field, useDefaultValue);
        } else {
            return generateValue(fieldType, useDefaultValue);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T generateValue(Class<T> fieldType, boolean useDefaultValue) {
        if (fieldType.isPrimitive()) {
            return (T) getPrimitiveDefaultValue(fieldType, useDefaultValue);
        } else if (fieldType.isArray()) {
            return generateArray(fieldType, useDefaultValue);
        } else if (fieldType.isEnum()) {
            return generateEnumFieldValue(fieldType, useDefaultValue);
        } else {
            return initWithValues(fieldType, useDefaultValue);
        }
    }

    private static <T> T generateEnumFieldValue(Class<T> enumType, boolean useDefaultValue) {
        T[] enumConstants = enumType.getEnumConstants();
        if (enumConstants.length == 0) {
            return null;
        } else {
            return useDefaultValue ? enumConstants[0] : enumConstants[RANDOM.nextInt(enumConstants.length)];
        }
    }

    @NotNull
    private static <T> T generateArray(Class<T> fieldType, boolean useDefaultValue) {
        Class<?> componentType = fieldType.getComponentType();
        int length = useDefaultValue ? 1 : RANDOM.nextInt(10);
        Object[] array = (Object[]) Array.newInstance(componentType, length);
        for (int i = 0; i < length; i++) {
            array[i] = generateValue(componentType, useDefaultValue);
        }
        return (T) array;
    }

    private static Object getPrimitiveDefaultValue(Class<?> type, boolean useDefaultValue) {
        if (!type.isPrimitive()) {
            throw new IllegalArgumentException("type: " + type + " is not a primitive type");
        }

        if (type == int.class) {
            return useDefaultValue ? 0 : RANDOM.nextInt();
        } else if (type == boolean.class) {
            return useDefaultValue || RANDOM.nextBoolean();
        } else if (type == long.class) {
            return useDefaultValue ? 0L : RANDOM.nextLong();
        } else if (type == float.class) {
            return useDefaultValue ? 0F : RANDOM.nextFloat();
        } else if (type == double.class) {
            return useDefaultValue ? 0D : RANDOM.nextDouble();
        } else if (type == char.class) {
            return useDefaultValue ? '\u0000' : (char) (RANDOM.nextInt(26) + 'a');
        } else if (type == byte.class) {
            byte[] bytes = new byte[1];
            RANDOM.nextBytes(bytes);
            return useDefaultValue ? 0 : bytes[0];
        } else if (type == short.class) {
            return useDefaultValue ? 0 : (short) RANDOM.nextInt(Short.MAX_VALUE + 1);
        }

        throw new UnsupportedOperationException("Type: " + type + " not supported");
    }


    public static <T> T initWithRandom(Class<T> clazz) {
        return (T) initWithValues(clazz, false);
    }

    public static void specifyCustomValueGenerator(Class<?> clazz, ValueGenerator<?> valueGenerator) {
        VALUE_GENERATORS.get().put(clazz, valueGenerator);
    }

    /**
     * We added an explicit type parameter Class<?> to the iterate method. This resolves the nested wildcard types.
     *
     * @param clazz any type parameter
     * @return the valid fields of the class
     */
    public static List<Field> getAllSettableFields(Class<?> clazz) {
        return Stream.<Class<?>>iterate(clazz, Objects::nonNull, Class::getSuperclass)
                .flatMap(c -> Arrays.stream(c.getDeclaredFields()))
                .filter(field -> !(Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())))
                .filter(field -> !(Modifier.isPrivate(field.getModifiers()) && Modifier.isFinal(field.getModifiers())))
                .filter(field -> !Modifier.isAbstract(field.getModifiers()))
                .filter(field -> !field.getType().isInterface())
                .collect(Collectors.toList());
    }

    public static <T> T init(Class<T> clazz) {
        try {
            return (T) UnsafeUtil.unsafe().allocateInstance(clazz);
        } catch (InstantiationException e) {
            throw new UnsupportedOperationException(String.format(
                    "Cannot initialize %s ,please add %s with the method called by registerCustomValueGenerator",
                    clazz.getName(), clazz.getSimpleName()));
        }
    }

    public interface ValueGenerator<T> {

        /**
         * Returns the value
         *
         * @param useDefaultValues whether to use default values
         * @return the value
         */
        T generateValue(boolean useDefaultValues);
    }
}
