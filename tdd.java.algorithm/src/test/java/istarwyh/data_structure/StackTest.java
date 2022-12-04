package istarwyh.data_structure;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.EmptyStackException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link StackTest} 使用JUint5 + 芝加哥学派的TDD 演进式驱动产出一个简单的 Stack
 *
 */
public class StackTest {

    @Test
    void can_new_a_stack(){
        new Stack();
    }

    @Nested
    class whenNew{

        private Stack stack;

        @BeforeEach
        void setUp() {
            stack = new Stack();
        }

        @Test
        void is_empty(){
            assertTrue(stack.isEmpty());
            assertEquals(0,stack.size());
        }

        @Test
        void should_throw_exception_when_popped(){
            assertThrows(
                    EmptyStackException.class,
                    stack::pop
            );
        }

        @Nested
        class AfterPushing{

            private final String anElement = "anElement";

            @Test
            void can_push_an_element(){
                stack.push(anElement);
                assertFalse(stack.isEmpty());
                assertEquals(1,stack.size());
            }

            @Test
            void push_two_element_and_size_is_two(){
                stack.push(anElement);
                stack.push(anElement);
                assertFalse(stack.isEmpty());
                assertEquals(2,stack.size());
            }

            @Nested
            class ThenPopping{

                private final String anotherElement = "anotherElement";

                @Test
                void should_be_empty_if_pushing_an_element_then_pop_it(){
                    stack.push(anElement);
                    stack.pop();
                    assertTrue(stack.isEmpty());
                    assertEquals(0,stack.size());
                }

                @Test
                void after_pushing_x_will_pop_x(){
                    stack.push(anElement);
                    assertSame(anElement, stack.pop());
                    stack.push(anotherElement);
                    assertSame(anotherElement, stack.pop());
                }

                @Test
                void after_pushing_x_and_y_will_pop_y_then_x(){
                    stack.push(anElement);
                    stack.push(anotherElement);
                    assertSame(anotherElement, stack.pop());
                    assertSame(anElement, stack.pop());
                }
            }
        }

    }
}
