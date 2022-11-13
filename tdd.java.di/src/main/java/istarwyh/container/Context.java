package istarwyh.container;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Context {
    private final Map<Class<?>, Supplier<?>> suppliers = new HashMap<>();

    public <ComponentType> void bind(Class<ComponentType> typeClass, ComponentType instance) {
        suppliers.put(typeClass, () -> instance);
    }

    public <ComponentType> ComponentType get(Class<ComponentType> typeClass) {
        return (ComponentType) suppliers.get(typeClass).get();
    }

    public <ComponentType,ComponentImplementation extends ComponentType>
    void bind(Class<ComponentType> type, Class<ComponentImplementation> componentImplementation) {
        suppliers.put(type, (Supplier<ComponentType>) () -> {
            try {
                return componentImplementation.getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
