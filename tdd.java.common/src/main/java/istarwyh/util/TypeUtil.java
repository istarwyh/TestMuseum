package istarwyh.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mac
 */
public class TypeUtil {
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
