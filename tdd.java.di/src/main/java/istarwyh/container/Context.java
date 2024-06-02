package istarwyh.container;

import istarwyh.exception.IllegalComponentException;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Arrays.stream;

/**
 * @author xiaohui
 */
public class Context {
    private final Map<Class<?>, Supplier<?>> suppliers = new HashMap<>();

    public <Type> void bind(Class<Type> typeClass, Type instance) {
        suppliers.put(typeClass, () -> instance);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <Type, Implementation extends Type>
    void bind(@NotNull Class<Type> type,@NotNull Class<Implementation> implementation) {
        Constructor<?>[] injectConstructors = stream(implementation.getConstructors())
                .filter(c -> c.isAnnotationPresent(Inject.class))
                .toArray(Constructor[]::new);
        if(injectConstructors.length > 1){
            throw new IllegalComponentException();
        }
        if(injectConstructors.length == 0 && hasDefaultConstructor(implementation)){
            throw new IllegalComponentException();
        }
        suppliers.put(type, (Supplier<Type>) () -> {
            try {
                Constructor<Implementation> constructor = (Constructor<Implementation>) stream(injectConstructors)
                        .filter(c -> c.isAnnotationPresent(Inject.class))
                        .findFirst()
                        .orElseGet(() -> {
                            try {
                                return implementation.getConstructor();
                            } catch (NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
                Object[] dependencies = stream(constructor.getParameters())
                        .map(p -> this.get(p.getType()))
                        .toArray(Object[]::new);
                return constructor.newInstance(dependencies);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static <Type, Implementation extends Type> boolean hasDefaultConstructor(Class<Implementation> implementation) {
        return stream(implementation.getConstructors()).noneMatch(c -> c.getParameters().length == 0);
    }

    @SuppressWarnings("unchecked")
    public <Type> Type get(Class<Type> typeClass) {
        return (Type) suppliers.get(typeClass).get();
    }

}
