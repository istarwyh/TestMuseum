package istarwyh.util;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalMap {

    @Getter
    private static final Map<String, String> map = new HashMap<>();

    static {
        map.put("k1","v1");
        map.put("k2","v2");
        map.put("k3","v3");
    }

    private static final ThreadLocal<Map<String, String>> THREAD_LOCAL = new ThreadLocal<>();

     static {
         THREAD_LOCAL.set(new HashMap<>());
         THREAD_LOCAL.get().put("k1","v1");
         THREAD_LOCAL.get().put("k2","v2");
         THREAD_LOCAL.get().put("k3","v3");
     }

     public static Map<String,String> getThreadLocalMap(){
         return THREAD_LOCAL.get();
     }
}
