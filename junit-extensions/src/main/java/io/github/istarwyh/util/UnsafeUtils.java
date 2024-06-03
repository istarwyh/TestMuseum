package io.github.istarwyh.util;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

/**
 * @author mac
 */
public class UnsafeUtils {

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
