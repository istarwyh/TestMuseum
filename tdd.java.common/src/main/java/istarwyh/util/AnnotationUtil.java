package istarwyh.util;

import istarwyh.util.annotation.Scale;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static istarwyh.util.ReflectionUtils.*;
import static java.util.Arrays.stream;

/**
 * @author xiaohui
 */
public class AnnotationUtil {

    /**
     *
     * @param object object owing the {@link Scale} annotation
     */
    public static void scaleFields(Object object) {
        stream(object.getClass().getDeclaredFields())
                .filter(it -> it.isAnnotationPresent(Scale.class))
                .forEach(it -> {
                    String fieldName = it.getName();
                    Scale annotation = it.getAnnotation(Scale.class);
                    try {
                        Field foundedField = findFieldInHierarchy(object, fieldName, anyField -> true).orElse(null);
                        if(foundedField == null){
                            return;
                        }
                        Object fieldValue = foundedField.get(object);
                        if (fieldValue == null) {
                            return;
                        }
                        Object scaledValue;
                        if (fieldValue instanceof BigDecimal) {
                            scaledValue = getScaledValue(annotation, fieldValue);
                        } else if (fieldValue instanceof List) {
                            scaledValue = ((List) fieldValue).stream()
                                    .map(e -> getScaledValue(annotation, e))
                                    .collect(Collectors.toList());
                        } else {
                            throw new UnsupportedOperationException("Field type: " + fieldValue.getClass().getSimpleName() +
                                    " is not supported by " + getCurrentMethodName());
                        }
                        setField(object, foundedField, scaledValue);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @NotNull
    private static BigDecimal getScaledValue(Scale annotation, Object field) {
        return ((BigDecimal) field).setScale(annotation.value(), annotation.roundingMode());
    }


}
