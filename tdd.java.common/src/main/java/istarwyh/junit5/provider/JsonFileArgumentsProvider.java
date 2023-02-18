package istarwyh.junit5.provider;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import istarwyh.junit5.annotation.JsonFileSource;
import istarwyh.junit5.provider.model.TestCase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.util.Preconditions;

import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

/**
 * @see <a href="https://github.com/joshka/junit-json-params">junit-json-params</a>
 */
public class JsonFileArgumentsProvider implements
        AnnotationConsumer<JsonFileSource>
        ,ArgumentsProvider
        ,ParameterResolver
{

    public static final String ADDRESS_DASH = "/";
    private final BiFunction<Class<?>, String, InputStream> inputStreamProvider;
    private static final String RESOURCES_PATH_PREFIX = "src/test/resources/";

    private String[] resources;

    private Class<?> type;
    private Class<?> ownClass;


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
        if(!(ownClass == Object.class)){
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
        return stream(resources)
                .map(resource -> openInputStream(context.getRequiredTestClass(),resource))
                .map(this::valueOfType)
                .map(Arguments::arguments);
    }

    @SneakyThrows
    private InputStream openInputStream(Class<?> testClass,String resource){
        InputStream inputStream;
        inputStream = inputStreamProvider.apply(testClass, resource);
        if(inputStream == null){
            CompletableFuture<String> resourceFuture = CompletableFuture.supplyAsync(() -> createTestReSource(resource));
            // avoid too long to end this process by io error
            inputStream = inputStreamProvider.apply(testClass, resourceFuture.get(2, TimeUnit.SECONDS));
        }
        return Preconditions.notNull(inputStream,
                () -> "Classpath resource does not exist: " + resource +", and we have created it");
    }

    private static String createTestReSource(String resource) {
        String filePath = RESOURCES_PATH_PREFIX + resource.substring(0, resource.lastIndexOf("/"));
        new File(filePath).mkdirs();
        try {
            String moduleAbsoluteResource = RESOURCES_PATH_PREFIX + resource;
            File file = new File(moduleAbsoluteResource);
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(moduleAbsoluteResource));
            writer.write(JSON.toJSONString(
                    new TestCase<>("This is your input","This is your expected output"))
            );
            writer.flush();
            writer.close();
            System.out.println(moduleAbsoluteResource + (file.exists() ? " created successfully" : "on way..."));
            return resource;
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
            throw new RuntimeException(e);
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