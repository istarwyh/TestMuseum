package istarwyh.container;

import jakarta.inject.Inject;
import lombok.Getter;
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
                context.bind(Component.class,ComponentWithInjectConstructor.class);
                Dependency dependency = new Dependency() {};
                context.bind(Dependency.class, dependency);

                Component instance = context.get(Component.class);
                assertNotNull(instance);
                assertSame(dependency,((ComponentWithInjectConstructor)instance).dependency());
            }

            // TODO A->B->C

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
    public ComponentWithNoArgsConstructor() {
    }
}

record ComponentWithInjectConstructor(@Getter Dependency dependency) implements Component {

    @Inject
    ComponentWithInjectConstructor {
    }
}
