package istarwyh.util;

import com.alibaba.fastjson2.JSON;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

/**
 * @author mac
 */
public class ObjectInit {

    public static <T> T initWithDefault(Class<T> clazz) {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .randomize(String.class, () -> "foo");
        EasyRandom easyRandom = new EasyRandom(parameters);
        return easyRandom.nextObject(clazz);
    }
}
