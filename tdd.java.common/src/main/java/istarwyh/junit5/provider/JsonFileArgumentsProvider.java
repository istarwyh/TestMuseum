package istarwyh.junit5.provider;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import istarwyh.junit5.annotation.JsonFileSource;
import istarwyh.junit5.provider.model.TestCase;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.util.Preconditions;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

/**
 * @author xiaohui
 * @see <a href="https://github.com/joshka/junit-json-params">junit-json-params</a>
 */
public class JsonFileArgumentsProvider implements
        AnnotationConsumer<JsonFileSource>
        ,ArgumentsProvider
        ,ParameterResolver {

    public static final String ADDRESS_DASH = "/";
    private final BiFunction<Class<?>, String, InputStream> inputStreamProvider;
    private static final String RESOURCES_PATH_PREFIX = "src/test/resources";

    private static final EasyRandom RANDOM = new EasyRandom();

    private String[] resources;

    private Class<?> type;
    private Class<?> ownClass;
    private Method requiredTestMethod;
    private Class<?> requiredTestClass;


    @SuppressWarnings("unused")
    JsonFileArgumentsProvider() {
        this(Class::getResourceAsStream);
    }

    JsonFileArgumentsProvider(BiFunction<Class<?>, String, InputStream> inputStreamProvider) {
        this.inputStreamProvider = inputStreamProvider;
    }

    private Object valueOfType(InputStream inputStream) {
        try (JSONReader reader = JSONReader.of(inputStream, Charset.defaultCharset())) {
            return reader.read(type);
        }
    }

    @Override
    public void accept(JsonFileSource jsonFileSource) {
        ownClass = jsonFileSource.in();
        resources = getResources(jsonFileSource);
        type = jsonFileSource.type();
    }

    private String[] getResources(JsonFileSource jsonFileSource) {
        String[] resources = jsonFileSource.resources();
        if(ownClass != Object.class){
            String packageName = ownClass.getPackageName();
            for(int i = 0; i < resources.length; i++){
                resources[i] = packageName.replaceAll("\\.",ADDRESS_DASH) + ADDRESS_DASH + resources[i];
            }
        }
        for(int i = 0; i < resources.length; i++){
            if(!resources[i].startsWith(ADDRESS_DASH)){
                resources[i] = ADDRESS_DASH + resources[i];
            }
        }
        return resources;
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        requiredTestMethod = context.getRequiredTestMethod();
        requiredTestClass = context.getRequiredTestClass();
        return stream(resources)
                .map(resource -> openInputStream(requiredTestClass,resource))
                .map(this::valueOfType)
                .map(Arguments::arguments);
    }

    @SneakyThrows(Exception.class)
    private InputStream openInputStream(Class<?> testClass,String resource){
        InputStream inputStream;
        inputStream = inputStreamProvider.apply(testClass, resource);
        if(inputStream == null){
            CompletableFuture<String> resourceFuture = CompletableFuture.supplyAsync(() -> createTestResource(resource));
            // avoid too long to end this process by io error
            inputStream = inputStreamProvider.apply(testClass, resourceFuture.get(5, TimeUnit.SECONDS));
        }
        return Preconditions.notNull(inputStream,
                () -> "*** Classpath resource does not exist: " + resource +", and we have created it ***");
    }

    private String createTestResource(String resource) {
        createDirectoryForResource(resource);
        return createFileAndWriteTestResource(resource);
    }

    private String createFileAndWriteTestResource(String resource) {
        try {
            String moduleAbsoluteResource = RESOURCES_PATH_PREFIX + resource;
            File file = createFile(moduleAbsoluteResource);
            writeTestResource2File(moduleAbsoluteResource, file);
            return resource;
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static File createFile(String moduleAbsoluteResource) throws IOException {
        File file = new File(moduleAbsoluteResource);
        if (!file.createNewFile()) {
            System.out.println("File already exists or could not be created: " + moduleAbsoluteResource);
        }
        return file;
    }

    @SneakyThrows(ClassNotFoundException.class)
    private void writeTestResource2File(String moduleAbsoluteResource, File file) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(moduleAbsoluteResource))){
            Type[] genericParameterTypes = requiredTestMethod.getGenericParameterTypes();
            Type genericParameterType = genericParameterTypes[0];
            Object parameterInstance = getParameterInstance(genericParameterType);
            writer.write(JSON.toJSONString(parameterInstance));
            writer.flush();
            System.out.println(moduleAbsoluteResource + (file.exists() ? "\ncreated successfully" : "on way..."));
        }
    }

    private static Object getParameterInstance(Type genericParameterType) throws ClassNotFoundException {
        Object object;
        if(genericParameterType instanceof  ParameterizedType){
            Type[] actualTypeArguments = ((ParameterizedType) genericParameterType).getActualTypeArguments();
            Pair<Class<?>, Class<?>> classPair = Pair.of(
                    Class.forName(actualTypeArguments[0].getTypeName()),
                    Class.forName(actualTypeArguments[1].getTypeName()));
            object = new TestCase<>(RANDOM.nextObject(classPair.getLeft()),RANDOM.nextObject(classPair.getRight()));
        }else {
            String typeName = genericParameterType.getTypeName();
            object = RANDOM.nextObject(Class.forName(typeName));
        }
        return object;
    }

    private static void createDirectoryForResource(String resource) {
        String fileDirPath = RESOURCES_PATH_PREFIX + resource.substring(0, resource.lastIndexOf("/"));
        if (!new File(fileDirPath).mkdirs()) {
            System.out.println("Directory already exists or could not be created: " + fileDirPath);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return false;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return null;
    }
}