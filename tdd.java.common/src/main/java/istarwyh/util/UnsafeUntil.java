package istarwyh.util;

import lombok.SneakyThrows;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeUntil {

    @SneakyThrows
    public static Unsafe unsafe(){
        return Singleton.unsafe;
    }

    private static class Singleton{
        private static final Unsafe unsafe;
        static  {
            Field unsafeFiled;
            try {
                unsafeFiled = Unsafe.class.getDeclaredField("theUnsafe");
                unsafeFiled.setAccessible(true);
                unsafe =  (Unsafe)unsafeFiled.get(Unsafe.class);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
