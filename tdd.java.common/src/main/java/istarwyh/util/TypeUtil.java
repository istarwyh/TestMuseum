package istarwyh.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

/**
 * @author mac
 */
public class TypeUtil {

    public static boolean isJsonType(Object value) {
        return value instanceof JSONObject || value instanceof JSONArray;
    }

}
