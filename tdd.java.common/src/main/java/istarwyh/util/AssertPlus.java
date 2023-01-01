package istarwyh.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.lang.reflect.Field;
import java.util.*;

import static istarwyh.util.AssertPlus.TypeUtil.isJsonType;
import static org.junit.jupiter.api.Assertions.*;

public class AssertPlus {

    /**
     * 递归遍历json对象所有的key-value，将其封装成path:value格式进行比较
     */
    private static void compareJson(Object expected, Object actual, boolean considerOrder) {
        Map<String, Object> oldMap = new LinkedHashMap<>();
        Map<String, Object> newMap = new LinkedHashMap<>();
        convertJsonToMap(expected, "", oldMap);
        convertJsonToMap(actual, "", newMap);
        if (considerOrder) {
            compareLinkedHashMapWithOrder(oldMap, newMap);
        } else {
            assertEquals(oldMap, newMap);
        }
    }

    public static void compareLinkedHashMapWithOrder(Map<String, Object> expected, Map<String, Object> actual) {
        Iterator<Map.Entry<String, Object>> iterator1 = expected.entrySet().iterator();
        Iterator<Map.Entry<String, Object>> iterator2 = actual.entrySet().iterator();

        while (iterator1.hasNext()) {
            Map.Entry<String, Object> entry1 = iterator1.next();
            Map.Entry<String, Object> entry2 = iterator2.next();
            compareFields(entry1.getKey(), entry2.getKey());
            compareFields(entry1.getValue(), entry2.getValue());
        }
    }

    /**
     * 将json数据转换为map存储用于比较
     *
     * @param json
     * @param root
     * @param resultMap
     */
    private static void convertJsonToMap(Object json, String root, Map<String, Object> resultMap) {
        if (json instanceof JSONObject jsonObject) {
            for (Object key : jsonObject.keySet()) {
                Object value = jsonObject.get(key);
                String newRoot = "".equals(root) ? key + "" : root + "." + key;
                if (isJsonType(value)) {
                    convertJsonToMap(value, newRoot, resultMap);
                } else {
                    resultMap.put(newRoot, value);
                }
            }
        } else if (json instanceof JSONArray jsonArray) {
            for (int i = 0; i < jsonArray.size(); i++) {
                Object value = jsonArray.get(i);
                String newRoot = "".equals(root) ? "[" + i + "]" : root + ".[" + i + "]";
                if (isJsonType(value)) {
                    convertJsonToMap(value, newRoot, resultMap);
                } else {
                    resultMap.put(newRoot, value);
                }
            }
        }
    }

    public static void compareFields(Object expected, Object actual) {
        if (eitherNullThenPass(expected, actual)) {
            return;
        }
        if (eitherJsonThenPass(expected, actual, false)) {
            return;
        }
        directCompareFields(expected, actual);
    }

    public static void compareFieldsWithOrder(Object expected, Object actual) {
        if (eitherNullThenPass(expected, actual)) {
            return;
        }
        if (eitherJsonThenPass(expected, actual, true)) {
            return;
        }
        directCompareFields(expected, actual);
    }

    private static void directCompareFields(Object expected, Object actual) {
        Class<?> aClass = expected.getClass();
        assertSame(aClass, actual.getClass());
        if (TypeUtil.isBuiltInType(expected)) {
            assertEquals(expected, actual);
        } else {
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                try {
                    String fieldName = field.getName();
                    Object valueA = ReflectionUtil.getField(expected, fieldName);
                    Object valueB = ReflectionUtil.getField(actual, fieldName);
                    compareFields(valueA, valueB);
                } catch (NoSuchElementException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static boolean eitherNullThenPass(Object a, Object b) {
        if (a == null || b == null) {
            assertNull(b);
            assertNull(a);
            return true;
        } else {
            return false;
        }
    }

    private static boolean eitherJsonThenPass(Object a, Object b, boolean considerOrder) {
        if (isJsonType(a) || isJsonType(b)) {
            compareJson(a, b, considerOrder);
            return true;
        } else {
            return false;
        }
    }


    public static class TypeUtil {
        private static final Set<Class<?>> BUILT_IN_TYPES = new HashSet<>(
                Arrays.asList(
                        Boolean.class, Byte.class, Character.class, Short.class,
                        Integer.class, Long.class, Float.class, Double.class,
                        boolean.class, byte.class, char.class, short.class,
                        int.class, long.class, float.class, double.class,
                        // 自定义内建类型
                        String.class
                )
        );

        public static boolean isBuiltInType(Object obj) {
            if (obj == null) {
                return false;
            }
            return BUILT_IN_TYPES.contains(obj.getClass());
        }

        public static boolean isJsonType(Object value) {
            return value instanceof JSONObject || value instanceof JSONArray;
        }

    }
}
