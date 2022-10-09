package istarwyh;

import istarwyh.exceptions.IllegalValueException;
import istarwyh.exceptions.InsufficientArgumentsException;
import istarwyh.exceptions.TooManyArgumentsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.function.Function;

import static istarwyh.OptionParserFactory.*;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class OptionParserFactoryTest {

    private Function<String, Object> parserWithoutWork;
    private Object whatever;

    @BeforeEach
    void setUp() {
        parserWithoutWork = (it) -> {
            throw new RuntimeException();
        };
        whatever = new Object();
    }

    @Nested
    class UnaryOptionParserTest{

        @Test// sad path
        void should_not_accept_extra_argument_for_single_valued_option() {
            final TooManyArgumentsException ex = assertThrows(TooManyArgumentsException.class, () -> {
                unary(Integer::parseInt, 0)
                        .parse(asList("-p", "8080", "8081"), option("p"));
            });
            assertEquals("p",ex.getOption());
        }

        @ParameterizedTest
        @ValueSource(strings = {"-p -l","-p"})// sad path
        void should_not_accept_insufficient_argument_for_single_valued_option(String arguments) {
            final InsufficientArgumentsException ex = assertThrows(InsufficientArgumentsException.class, () -> {
                unary(String::valueOf, "")
                        .parse(asList(arguments.split(" ")), option("p"));
            });
            assertEquals("p",ex.getOption());
        }

        @Test//default value
        void should_set_default_value_when_not_exist_arguments(){
            Function<String,Object> whatever = (it) -> null;
            Object defaultValue = new Object();
            assertSame(defaultValue, unary(whatever, defaultValue).parse(Collections.emptyList(),option("p")));
        }

        @Test// happy path
        public void should_parse_value_as_if_flag_present(){
            Object parsed = new Object();
            Function<String,Object> parse = (it) -> parsed;
            assertSame(parsed, unary(parse, whatever).parse(asList("-p","8080"),option("p")));
        }

        @Test//exception
        void should_throw_exception_if_value_parser_cannot_parse_value(){

            final IllegalValueException exception = assertThrows(
                    IllegalValueException.class,
                    () -> unary(parserWithoutWork, whatever).parse(asList("-p", "8080"), option("p"))
            );

            assertEquals("p",exception.getOption());
            assertEquals("8080",exception.getValue());
        }

    }

    @Nested
    class BoolParserTest {

        @Test// sad path
        void should_not_accept_extra_argument_for_boolean_option() {
            TooManyArgumentsException ex = assertThrows(TooManyArgumentsException.class,() ->{
                bool().parse(asList("-l","t"),option("l"));
            });

            assertEquals("l",ex.getOption());
        }

        @Test// default value
        void should_set_default_value_to_false_if_option_is_not_present() {
            final Boolean o = bool().parse(Collections.emptyList(), option("l"));
            assertFalse(o);
        }

        @Test// happy path
        public void should_set_boolean_option_to_true_if_flag_present(){
            assertTrue(bool().parse(Collections.singletonList("-l"),option("l")));
        }

    }

    @Nested
    class ListOptionParserTest{

        @Test
        void should_parse_list_value(){
            assertArrayEquals(new String[]{"this","is"},
                    list(String::valueOf, String[]::new).parse(asList("-g", "this", "is"), option("g")));
        }

        @Test
        void should_return_empty_array_as_default_value(){
            assertArrayEquals(new String[]{},
                    list(String::valueOf, String[]::new).parse(Collections.emptyList(), option("g")));
        }

        @Test
        void should_throw_exception_if_value_parser_cannot_parse_value(){
            final IllegalValueException illegalValueException = assertThrows(
                    IllegalValueException.class,
                    () -> list(parserWithoutWork, Object[]::new).parse(asList("-g", "this", "is"), option("g"))
            );

            assertEquals("g",illegalValueException.getOption());
            assertEquals("this",illegalValueException.getValue());
        }

        @Test
        void should_not_treat_negative_values_as_flag(){
            assertArrayEquals(new Integer[]{-1,-2},
                    list(Integer::parseInt,Integer[]::new).parse(asList("-d","-1","-2"),option("d")));
        }

    }

    static Option option(String value){
        return new Option(){

            @Override
            public Class<? extends Annotation> annotationType() {
                return Option.class;
            }

            @Override

            public String value() {
                return value;
            }
        };
    }
}