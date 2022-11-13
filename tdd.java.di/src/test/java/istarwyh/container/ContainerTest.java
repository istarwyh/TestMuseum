package istarwyh.container;

import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ContainerTest {

    private Context context;

    @BeforeEach
    void setUp() {
        context = new Context();
    }

    @Nested
    class ComponentConstruction{
        // TODO instance
        @Test
        void should_bind_type_to_a_specific_instance(){
            Component instance = new Component() {};
            context.bind(Component.class,instance);
            assertSame(instance, context.get(Component.class));
        }

        // TODO abstract class
        // TODO interface

        @Nested
        class ConstructorInjection{

            @Test
            void should_bind_type_to_a_class_with_default_constructor(){
                context.bind(Component.class,ComponentWithNoArgsConstructor.class);
                Component instance = context.get(Component.class);
                assertNotNull(instance);
            }

            @Test
            void should_bind_type_to_a_class_with_injected_constructor(){
                Dependency dependency = new Dependency() {};

                context.bind(Component.class,ComponentWithInjectConstructor.class);
                context.bind(Dependency.class, dependency);

                Component instance = context.get(Component.class);
                assertNotNull(instance);
                assertSame(dependency,((ComponentWithInjectConstructor)instance).dependency());
            }

            @Test
            void should_bing_type_a_class_with_transitive_dependencies(){
                context.bind(Component.class,ComponentWithInjectConstructor.class);
                context.bind(Dependency.class, DependencyWithInjectConstructor.class);
                String indirectDependency = "indirect indirectDependency";
                context.bind(String.class, indirectDependency);

                Component instance = context.get(Component.class);
                assertNotNull(instance);
                Dependency dependency = ((ComponentWithInjectConstructor) instance).dependency();
                assertNotNull(dependency);
                assertEquals(indirectDependency,((DependencyWithInjectConstructor)dependency).indirectDependency());
            }

            @Test
            void should_throw_exception_if_multi_inject_constructors_provided(){
                assertThrows(
                        IllegalComponentException.class,
                        () -> context.bind(Component.class,ComponentWithMultiConstructor.class)
                );
            }

            @Test
            void should_throw_exception_if_no_inject_nor_default_constructor_provided(){
                assertThrows(
                        IllegalComponentException.class,
                        () -> context.bind(Component.class, ComponentWithNoInjectNorDefaultConstructor.class)
                );
            }
            // todo dependency not exist

        }

        @Nested
        class FieldInjection{

        }

        @Nested
        class MethodInjection{

        }
    }

}

interface Component{}
interface Dependency{}

class ComponentWithNoArgsConstructor implements Component{
    public ComponentWithNoArgsConstructor() {}
}

record ComponentWithInjectConstructor(Dependency dependency) implements Component {

    @Inject
    public ComponentWithInjectConstructor{}
}

record DependencyWithInjectConstructor(String indirectDependency) implements Dependency{

    @Inject
    public DependencyWithInjectConstructor{}
}

class ComponentWithMultiConstructor implements Component{

    @Inject
    public ComponentWithMultiConstructor(String name){}

    @Inject
    public ComponentWithMultiConstructor(String name,Double value){}
}

class ComponentWithNoInjectNorDefaultConstructor implements Component{
    public ComponentWithNoInjectNorDefaultConstructor(String name) {}
}