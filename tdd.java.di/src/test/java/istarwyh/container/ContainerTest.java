package istarwyh.container;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ContainerTest {

    interface Component{}

    static class ComponentWithNoArgsConstructor implements Component{
        public ComponentWithNoArgsConstructor() {
        }
    }

    @Nested
    class ComponentConstruction{
        // TODO instance
        @Test
        void should_bind_type_to_a_specific_instance(){
            Context context = new Context();
            Component instance = new Component() {};
            context.bind(Component.class,instance);
            assertSame(instance,context.get(Component.class));
        }

        // TODO abstract class
        // TODO interface

        @Nested
        class ConstructorInjection{
            // TODO No args
            @Test
            void should_bind_type_to_a_instance_with_default_constructor(){
                Context context = new Context();
                context.bind(Component.class,ComponentWithNoArgsConstructor.class);
                Component instance = context.get(Component.class);
                assertNotNull(instance);
            }
            // TODO args
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
