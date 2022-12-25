package istarwyh.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AssertPlus {

    public static void compareFields(Object a, Object b){
        if(a == null || b == null){
            assertNull(b);
            assertNull(a);
            return;
        }
        Field[] fields = a.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                Object valueA = ReflectionUtil.getField(a,field.getName());
                Object valueB = ReflectionUtil.getField(b,field.getName());
                if (valueA == null || valueB == null){
                    assertNull(valueB);
                    assertNull(valueA);
                }else {
                    assertSame(valueA.getClass(), valueB.getClass());
                    if(TypeUtils.isBuiltInType(valueA)){
                        assertEquals(valueA,valueB);
                    }else {
                        AssertPlus.compareFields(valueA,valueB);
                    }
                }
            } catch (NoSuchElementException e) {
               throw new RuntimeException(e);
            }
        }
    }


    public static class TypeUtils {
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
