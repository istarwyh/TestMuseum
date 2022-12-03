package istarwyh.provider;

import com.alibaba.fastjson2.JSONReader;
import istarwyh.junit5.annotation.JsonFileSource;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.util.Preconditions;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class JsonFileArgumentsProvider implements
        AnnotationConsumer<JsonFileSource>,
        ArgumentsProvider
    , ParameterResolver
{

    public static final String ADDRESS_DASH = "/";
    private final BiFunction<Class<?>, String, InputStream> inputStreamProvider;

    private String[] resources;

    private Class<?> type;

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
        resources = getResources(jsonFileSource);
        type = jsonFileSource.type();
    }

    private static String[] getResources(JsonFileSource jsonFileSource) {
        String[] resources = jsonFileSource.resources();
        for(int i =0; i < resources.length; i++){
            if(!resources[i].startsWith(ADDRESS_DASH)){
                resources[i] = ADDRESS_DASH + resources[i];
            }
        }
        return resources;
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return stream(resources)
                .map(resource -> openInputStream(context, resource))
                .map(this::valueOfType)
                .map(Arguments::arguments);
    }

    private InputStream openInputStream(ExtensionContext context, String resource) {
        Class<?> testClass = context.getRequiredTestClass();
        InputStream inputStream = inputStreamProvider.apply(testClass, resource);
        return Preconditions.notNull(inputStream,
                () -> "Classpath resource does not exist: " + resource);
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