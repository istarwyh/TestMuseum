package istarwyh.util;

import lombok.SneakyThrows;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author mac
 */
public class UnsafeUtil {

    public static Unsafe unsafe(){
        return Singleton.UNSAFE;
    }

    private static class Singleton{
        private static final Unsafe UNSAFE;
        static  {
            Field unsafeFiled;
            try {
                unsafeFiled = Unsafe.class.getDeclaredField("theUnsafe");
                unsafeFiled.setAccessible(true);
                UNSAFE =  (Unsafe)unsafeFiled.get(Unsafe.class);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
