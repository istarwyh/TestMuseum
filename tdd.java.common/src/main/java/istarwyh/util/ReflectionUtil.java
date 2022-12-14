package istarwyh.util;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Optional;
import java.util.function.Predicate;

import static istarwyh.util.UnsafeUntil.unsafe;


/**
 * This class is changed from WhileBox in PowerMock.
 * The class can set instance field,including final field, not null or static field
 * @see <a href=https://www.baeldung.com/mockito-mock-static-methods>Mockito#mockStatic</a>
 */
public class ReflectionUtil {

    /**
     *
     * @param modifiedObj modifiedObj
     * @param fieldName including final field, not null or static field
     * @param value value
     */
    public static void setField(Object modifiedObj,String fieldName,Object value) throws NoSuchFieldException {
        Field foundField = findFieldInHierarchy(modifiedObj,fieldName, field -> hasFieldProperModifier(modifiedObj, field));
        setField(modifiedObj,foundField,value);
    }

    @SneakyThrows
    public static <T> T getField(Object object,String fieldName){
        Field foundField = findFieldInHierarchy(object,fieldName,anyField -> true);
        return (T)foundField.get(object);
    }

    private static Field findFieldInHierarchy(Object modifiedObj, String fieldName,
                                              @NotNull Predicate<Field> fieldPredicate) throws NoSuchFieldException {
        if(modifiedObj == null){
            throw new IllegalArgumentException("The modifiedObj containing the field cannot be null!");
        }
        Class<?> startClass = getClassOf(modifiedObj);
        Field foundField = findField(fieldName,startClass)
                .filter(fieldPredicate)
                .orElseThrow(() -> new NoSuchFieldException(String.format(
                        "No %s field named \"%s\" could be found in the \"%s\" class hierarchy",
                        isClass(modifiedObj) ? "static" : "instance",fieldName,startClass.getName()
                )));
        foundField.setAccessible(true);
        return foundField;
    }

    private static Optional<Field> findField(String fieldName, Class<?> startClass) {
        Field foundField = null;
        while (startClass != null){
            Field[] declaredFields = startClass.getDeclaredFields();
            for(var field : declaredFields){
                if(fieldName.equals(field.getName())){
                    if(foundField != null){
                        throw new IllegalStateException("Two or more field matching " + fieldName + ".");
                    }
                    foundField = field;
                }
            }
            if(foundField != null){
                break;
            }
            startClass = startClass.getSuperclass();
        }
        return Optional.ofNullable(foundField);
    }

    private static boolean hasFieldProperModifier(Object object, Field field) {
        if(isClass(object)){
            return Modifier.isStatic(field.getModifiers());
        }else {
            return !Modifier.isStatic(field.getModifiers());
        }
    }

    private static Class<?> getClassOf(@NotNull Object object) {
        Class<?> type;
        if(isClass(object)){
            type = (Class<?>) object;
        } else{
            type = object.getClass();
        }
        return type;
    }

    private static boolean isClass(Object object) {
        return object instanceof Class<?>;
    }

    private static void setField(Object object, Field foundField, Object value) {
        boolean isStatic = isModifier(foundField,Modifier.STATIC);
        Unsafe unsafe = unsafe();
        if(isStatic) setStaticFieldUsingUnsafe(foundField,value);
        else setFieldUsingUnsafe(object,foundField,unsafe.objectFieldOffset(foundField),value);
    }

    private static void setStaticFieldUsingUnsafe(Field field, Object value) {
        Object base = unsafe().staticFieldBase(field);
        long offset = unsafe().staticFieldOffset(field);
        setFieldUsingUnsafe(base,field,offset,value);
    }

    /**
     * judge whether modifier the field belongs to
     * @param field field
     * @param modifier {@link Modifier#STATIC }.etc
     * @return if modifier the field belongs to
     */
    private static boolean isModifier(Field field, int modifier) {
        return (field.getModifiers() & modifier) == modifier;
    }

    @SneakyThrows
    @SuppressWarnings("all")
    private static void setFieldUsingUnsafe(Object base, Field field, long offset, Object newValue) {
        field.setAccessible(true);
        boolean isFinal = isModifier(field, Modifier.FINAL);
        if(isFinal){
            AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                    setFieldUsingUnsafe(base,field.getType(),offset,newValue);
                    return null;
                }
            );
        }else {
            field.set(base,newValue);
        }
    }

    private static void setFieldUsingUnsafe(Object base, Class<?> type, long offset, Object newValue) {
        if(type == Integer.TYPE){
            unsafe().putInt(base,offset,(Integer)newValue);
        }else if(type == Short.TYPE){
            unsafe().putShort(base,offset,(Short)newValue);
        } else if (type == Long.TYPE) {
            unsafe().putLong(base,offset,(Long)newValue);
        } else if (type == Byte.TYPE) {
            unsafe().putByte(base,offset,(Byte)newValue);
        }else if(type == Boolean.TYPE){
            unsafe().putBoolean(base,offset,(Boolean)newValue);
        } else if (type == Float.TYPE) {
            unsafe().putFloat(base,offset,(Float)newValue);
        } else if (type == Double.TYPE) {
            unsafe().putDouble(base,offset,(Double) newValue);
        }else if (type == Character.TYPE){
            unsafe().putChar(base,offset,(Character)newValue);
        }else {
            unsafe().putObject(base,offset,newValue);
        }
    }
}
