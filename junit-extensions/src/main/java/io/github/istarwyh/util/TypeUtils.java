package io.github.istarwyh.util;

import com.alibaba.fastjson.JSON;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mac
 */
public class TypeUtils {

    private static final Set<Class<?>> BUILT_IN_TYPES = new HashSet<>(
            Arrays.asList(
                    Boolean.class, Byte.class, Character.class, Short.class,
                    Integer.class, Long.class, Float.class, Double.class,
                    boolean.class, byte.class, char.class, short.class,
                    int.class, long.class, float.class, double.class
            )
    );

    public static boolean isBuiltInType(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return BUILT_IN_TYPES.contains(clazz);
    }


    public static boolean isJsonType(Object value) {
        return value instanceof JSON;
    }

}
