package istarwyh.container;

import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Arrays.stream;

public class Context {
    private final Map<Class<?>, Supplier<?>> suppliers = new HashMap<>();

    public <Type> void bind(Class<Type> typeClass, Type instance) {
        suppliers.put(typeClass, () -> instance);
    }

    public <Type, Implementation extends Type>
    void bind(Class<Type> type, Class<Implementation> implementation) {
        suppliers.put(type, (Supplier<Type>) () -> {
            try {
                Constructor<Implementation> constructor = getInjectOrDefaultConstructor(implementation);
                Object[] dependencies = stream(constructor.getParameters())
                        .map(p -> this.get(p.getType()))
                        .toArray(Object[]::new);
                return constructor.newInstance(dependencies);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @NotNull
    private static <Type, Implementation extends Type> Constructor<Implementation>
    getInjectOrDefaultConstructor(Class<Implementation> implementation) {
        Optional<Constructor<?>> firstInjectConstructor = stream(implementation.getConstructors())
                .filter(c -> c.isAnnotationPresent(Inject.class))
                .findFirst();

        return (Constructor<Implementation>) firstInjectConstructor.orElseGet(() -> {
                    try {
                        return implementation.getConstructor();
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    public <Type> Type get(Class<Type> typeClass) {
        return (Type) suppliers.get(typeClass).get();
    }

}
