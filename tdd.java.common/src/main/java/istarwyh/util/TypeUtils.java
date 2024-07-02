package istarwyh.util;

import static org.apache.commons.lang3.ClassUtils.isAssignable;
import static org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.util.Collection;
import java.util.Map;

/**
 * @author mac
 */
public class TypeUtils {

    public static boolean isBuiltInType(Class<?> clazz) {
        return isPrimitiveOrWrapper(clazz);
    }

    public static boolean isJsonType(Object value) {
        return value instanceof JSONObject || value instanceof JSONArray;
    }

    public static boolean cannotGetAccess(Class<?> clazz) {
        
        return isPrimitiveOrWrapper(clazz) ||
                isAssignable(clazz, Collection.class) || 
                isAssignable(clazz, Map.class) || 
                isAssignable(clazz, Number.class) || 
                isAssignable(clazz, String.class) || 
                clazz.isEnum() || 
                clazz.isMemberClass() ||
                clazz.isLocalClass() ||
                clazz.isArray() || 
                clazz.isAnnotation() || 
                clazz.isAnonymousClass() || 
                clazz.isInterface() || 
                clazz.isSynthetic() ;
    }
}
