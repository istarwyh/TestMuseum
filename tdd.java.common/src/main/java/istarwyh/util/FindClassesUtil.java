package istarwyh.util;

import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FindClassesUtil {

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
    private static List<Class<?>> findClasses(File directory, String packageName) {
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


}
