package istarwyh.util;

import static istarwyh.util.UnsafeUtil.unsafe;

import java.io.File;
import java.lang.reflect.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import sun.misc.Unsafe;

/**
 * This class is changed from WhileBox in PowerMock.
 * The class can set instance field,including final field, not null or static field
 * @author xiaohui
 * @see <a href=https://www.baeldung.com/mockito-mock-static-methods>Mockito#mockStatic</a>
 */
public class ReflectionUtil {

    /**
     * eg. lambda$scaleFields$3
     * @return the method name of calling this method
     */
    public static String getCurrentMethodName() {
        return Arrays.stream(Thread.currentThread().getStackTrace())
                .map(StackTraceElement::getMethodName)
                // skip getCurrentMethodName() and getStackTrace()
                .skip(2)
                .findFirst()
                .orElse(null);
    }

    @SneakyThrows({NoSuchMethodException.class, InvocationTargetException.class,InstantiationException.class,IllegalAccessException.class})
    public static <T> T getInstanceWithoutArgs(Class<T> clazz) {
        // 获取无参构造方法（如果有参数，则传入对应的 Class 类型作为参数）
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        // 当构造方法为 private 时，需要设置可访问
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    /**
     * If you want to set static field, you should call {@link ReflectionUtil#setField(Object, Field, Object)}
     * @param modifiedObj modifiedObj
     * @param fieldName including final field, not null or static field
     * @param value value
     */
    @SneakyThrows(NoSuchFieldException.class)
    public static void setField(Object modifiedObj,String fieldName,Object value) {
        Field foundField = findFieldInHierarchy(modifiedObj,fieldName, field -> hasFieldProperModifier(modifiedObj, field));
        setField(modifiedObj,foundField,value);
    }

    @SneakyThrows({NoSuchFieldException.class,IllegalAccessException.class})
    public static <T> T getField(Object object,String fieldName){
        Field foundField = findFieldInHierarchy(object,fieldName,anyField -> true);
        return (T)foundField.get(object);
    }

    public static Field findFieldInHierarchy(Object modifiedObj, String fieldName,
                                              @NotNull Predicate<Field> fieldPredicate) throws NoSuchFieldException {
        if(modifiedObj == null){
            throw new IllegalArgumentException("The modifiedObj containing the field cannot be null!");
        }
        Class<?> startClass = getClassOf(modifiedObj);
        Field foundField = findFieldByUniqueName(fieldName,startClass)
                .filter(fieldPredicate)
                .orElseThrow(() -> new NoSuchFieldException(String.format(
                        "No %s field named \"%s\" could be found in the \"%s\" class hierarchy",
                        isClass(modifiedObj) ? "static" : "instance",fieldName,startClass.getName()
                )));
        foundField.setAccessible(true);
        return foundField;
    }

    public static Optional<Field> findFieldByUniqueName(String fieldName, Class<?> startClass) {
        FieldSearchCriteria criteria = new FieldSearchCriteria(
                startClass,
                field -> field.getName().equals(fieldName),
                fieldName);
        return findField(criteria);
    }

    public static Optional<Field> findFieldByUniqueType(Type fieldType, Class<?> startClass) {
        FieldSearchCriteria criteria = new FieldSearchCriteria(startClass,
                field -> field.getGenericType().equals(fieldType),
                fieldType.getTypeName());
        return findField(criteria);
    }

    private static Optional<Field> findField(FieldSearchCriteria criteria) {
        Field foundField = null;
        Class<?> currentClass = criteria.startClass();
        while (currentClass != null) {
            Field[] declaredFields = currentClass.getDeclaredFields();
            for (var field : declaredFields) {
                if (criteria.matcher().apply(field)) {
                    if (foundField != null) {
                        throw new IllegalStateException("Two or more fields matching " + criteria.errorMessage() + ".");
                    }
                    foundField = field;
                }
            }
            if (foundField != null) {
                break;
            }
            currentClass = currentClass.getSuperclass();
        }
        return Optional.ofNullable(foundField);
    }
    public record FieldSearchCriteria(Class<?> startClass, Function<Field, Boolean> matcher, String errorMessage) {}

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

    public static void setField(Object object, Field foundField, Object value) {
        boolean isStatic = isModifier(foundField,Modifier.STATIC);
        Unsafe unsafe = unsafe();
        if(isStatic) {
            setStaticFieldUsingUnsafe(foundField,value);
        } else {
            setFieldUsingUnsafe(object,foundField,unsafe.objectFieldOffset(foundField),value);
        }
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

    /**
     *
     * @param interfaceClass a interface class
     * @return a list of class that implements the interface
     * @param <T> eg. {@link String}
     */
    public static <T> List<Class<? extends T>> getAllClassesImplementingInterface(Class<T> interfaceClass) {
        String packageName = interfaceClass.getPackageName();
        // 获取指定包下的所有类
        List<Class<?>> classes = getClasses(packageName);
        // 使用 Lambda 表达式筛选出实现了指定接口的类，并转换为对应的 Class 对象
        return classes.stream()
                .filter(clazz -> interfaceClass.isAssignableFrom(clazz) && !clazz.equals(interfaceClass))
                .map(clazz -> (Class<? extends T>) clazz)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param packageName eg. "java.util"
     * @return eg. "java.util.List"
     */
    @SneakyThrows
    public static List<Class<?>> getClasses(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        List<File> dirs = Collections.list(classLoader.getResources(path)).stream()
                .map(URL::getFile)
                .map(File::new)
                .toList();
        return dirs.stream()
                .flatMap(file -> findClasses(file, packageName).stream())
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public static List<Class<?>> findClasses(File directory, String packageName) {
        Path directoryPath = directory.toPath();
        try (Stream<Path> pathStream = Files.walk(directoryPath)) {
            return pathStream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".class"))
                    .map(path -> {
                        try {
                            // 获取相对路径并转换为全限定类名
                            Path relativePath = directoryPath.relativize(path);
                            String className = packageName + "." + relativePath.toString().replace(".class", "")
                                    .replace(File.separatorChar, '.');
                            return Class.forName(className);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
        }
    }

    public static <POJO> List<ImmutablePair<String, Object>> getPojoFieldNameAndValue(POJO pojo) {
        return getPojoFieldNameAndValue(pojo, any -> true);
    }

    public static <POJO> List<ImmutablePair<String, Object>>
    getPojoFieldNameAndValue(POJO pojo, Predicate<Field> fieldPredicate) {
        List<Field> fields = Arrays.stream(pojo.getClass().getDeclaredFields())
                .filter(fieldPredicate)
                .peek(field -> field.setAccessible(true))
                .toList();
        return fields.stream()
                .map(field -> {
                    try {
                        Object value = field.get(pojo);
                        return new ImmutablePair<>(field.getName(), value);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    @NotNull
    public static List<Field> getAllFields(Class<?> clazz, Predicate<Class<?>> filterClazz) {
        return Stream.<Class<?>>iterate(clazz, Objects::nonNull, Class::getSuperclass)
                .filter(filterClazz)
                .flatMap(it -> Arrays.stream(it.getDeclaredFields()))
                .peek(it -> it.setAccessible(true))
                .toList();
    }

    @NotNull
    public static Stream<Object> getDeclaredFields(Object obj) {
        return Stream.of(obj.getClass().getDeclaredFields())
                .map(field -> {
                    try { return field.get(obj); }
                    catch (IllegalAccessException e) { throw new RuntimeException(e); }
                });
    }


    /**
     * find the first generic clazz of the given interface
     * @param originInterface the generic interface
     * @param concreteClass the concrete class the implemented the generic interface
     * @return the generic interface generic clazz
     * @param <T> the generic interface generic param,like `Interface Example<T>`
     */
    public static <T> Class<T> getInterfaceFirstGenericClazz(Class<?> originInterface, Class<?> concreteClass) {
        for(Type type : concreteClass.getGenericInterfaces()){
            if(type instanceof ParameterizedType &&
                    ((ParameterizedType) type).getRawType().getTypeName().equals(originInterface.getName())){
                Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
                if(actualTypeArgument != null){
                    return getClassFromParameterizedType(actualTypeArgument);
                }
            }
        }
        throw new IllegalArgumentException("Invalid interface");
    }

    @NotNull
    private static <T>  Class<T> getClassFromParameterizedType(Type actualTypeArgument) {
        if(actualTypeArgument instanceof Class){
            return (Class<T>) actualTypeArgument;
        }else {
            return getClassFromParameterizedType(((ParameterizedType)actualTypeArgument).getRawType());
        }
    }

    /**
     *
     * parseGenericTypesFromSignature
     * @param str method signature like "(Listarwyh/junit5/provider/model/TestCase<Ljava/lang/String;Ljava/lang/String;>;)V"
     * @return (Class)
     */
    @SneakyThrows(ClassNotFoundException.class)
    public static Pair<Class<?>, Class<?>> parseGenericTypesFromSignature(String str) {
        int firstIndex = str.indexOf("<L") + 2;
        int lastIndex = str.lastIndexOf(">;");
        String[] classes = str.substring(firstIndex, lastIndex).split(";L");
        Class<?> firstClass = Class.forName(classes[0].replace('/', '.'));
        Class<?> secondClass = Class.forName(classes[1].replace('/', '.'));
        return Pair.of(firstClass, secondClass);
    }

    /**
     * We added an explicit type parameter Class<?> to the iterate method. This resolves the nested wildcard types.
     *
     * @param clazz any type parameter
     * @return the valid fields of the class
     */
    public static List<Field> getAllSettableFields(Class<?> clazz) {
        return Stream.<Class<?>>iterate(clazz, Objects::nonNull, Class::getSuperclass)
                .flatMap(c -> Arrays.stream(c.getDeclaredFields()))
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(field -> !Modifier.isFinal(field.getModifiers()))
                .filter(field -> !Modifier.isAbstract(field.getModifiers()))
                .filter(field -> !Modifier.isNative(field.getModifiers()))
                .filter(field -> !Modifier.isTransient(field.getModifiers()))
                .filter(field -> !field.getType().isInterface())
                .collect(Collectors.toList());
    }
}
