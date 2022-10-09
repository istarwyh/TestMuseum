package istarwyh;

import istarwyh.exceptions.IllegalAnnotationException;
import istarwyh.exceptions.UnsupportedOptionTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArgsTest {

    @Test
    void should_parse_multi_option_when_unary_value(){

        MultiOptions options = Args.parse(MultiOptions.class,"-l","-p","8080","-d","/usr/logs");
        assertTrue(options.logging());
        assertEquals(8080,options.port());
        assertEquals("/usr/logs",options.directory());
    }

    public static record MultiOptions(@Option("l") boolean logging, @Option("p") int port, @Option("d") String directory){}

    @Test
    void should_throw_illegal_annotation_exception_when_annotation_absent(){
        assertThrows(IllegalAnnotationException.class,()->{
            Args.parse(OptionsWithoutAnnotation.class,"-l","-p","8080","-d","/usr/logs");
        });
    }

    public static record OptionsWithoutAnnotation(@Option("l") boolean logging, int port, @Option("d") String directory){}


    @Test// exception
    void should_throw_exception_when_option_not_match_parameter_type(){
        final UnsupportedOptionTypeException unsupportedOptionTypeException = assertThrows(
                UnsupportedOptionTypeException.class,
                () -> Args.parse(OptionWithUnsupportedType.class, "-d", "1")
        );
        assertEquals("d",unsupportedOptionTypeException.getOption());
        assertEquals(Object.class,unsupportedOptionTypeException.getType());
    }

    static record OptionWithUnsupportedType(@Option("d") Object decimals){}

    @Test
    @DisplayName("-g this is a list -d 1 2 -3 5")
    void should_parse_multiple_options_when_value_list(){

        ListOptions listOptions = Args.parse(ListOptions.class,"-g","this","is","a","list","-d","-1","-2","3","6");

        assertArrayEquals(new String[]{"this","is","a","list"},listOptions.group());
        assertArrayEquals(new Integer[]{-1,-2,3,6},listOptions.decimals());
    }

    static record ListOptions(@Option("g") String[] group,@Option("d") Integer[] decimals){}

}