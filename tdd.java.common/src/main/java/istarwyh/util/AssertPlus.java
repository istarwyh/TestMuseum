package istarwyh.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AssertPlus {

    public static void compareFields(Object a, Object b){
        if (eitherNull(a, b)){return;}
        Class<?> aClass = a.getClass();
        assertSame(aClass, b.getClass());
        if(TypeUtil.isBuiltInType(a)) {
            assertEquals(a, b);
        }else {
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                try {
                    String fieldName = field.getName();
                    Object valueA = ReflectionUtil.getField(a, fieldName);
                    Object valueB = ReflectionUtil.getField(b, fieldName);
                    compareFields(valueA,valueB);
                } catch (NoSuchElementException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static boolean eitherNull(Object a, Object b) {
        if(a == null || b == null){
            assertNull(b);
            assertNull(a);
            return true;
        }else {
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
    }
}
