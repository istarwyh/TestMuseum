package istarwyh.util;

import istarwyh.util.annotation.Scale;

import java.math.BigDecimal;
import java.util.Arrays;

public class AnnotationUtil {

    /**
     *
     * @param object object owing the {@link Scale} annotation
     */
    public static void forScaleAnnotation(Object object){
        Arrays.stream(object.getClass().getDeclaredFields())
                .filter(it -> it.isAnnotationPresent(Scale.class))
                .forEach(it -> {
                    String fieldName = it.getName();
                    BigDecimal field = ReflectionUtil.getField(object, fieldName);
                    Scale annotation = it.getAnnotation(Scale.class);
                    BigDecimal bigDecimal = field.setScale(annotation.value(), annotation.roundingMode());
                    try {
                        ReflectionUtil.setField(object, fieldName, bigDecimal);
                    } catch (NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                });
    }


}
