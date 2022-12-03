package istarwyh.provider;

import com.alibaba.fastjson2.TypeReference;
import istarwyh.junit5.annotation.JsonFileSource;
import istarwyh.provider.model.TestCase;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonFileArgumentsProviderTest {

    @ParameterizedTest
    @CsvSource(value = {
            "1,2",
            "3,4"
    })
    void should_show_how_to_parse_multi_args_with_csv(Integer in,Integer out){
        assertEquals(out,in + 1);
    }

    @ParameterizedTest
    @JsonFileSource(type = TestCase.class,resources = {"/list_testCase.json"})
    void should_parse_List_type_test_case(TestCase<List<Integer>,List<Integer>> testCase){
        assertEquals("[1,2,3]",testCase.input.toString());
        assertEquals("[3,4,5]",testCase.output.toString());
    }

    @ParameterizedTest
    @JsonFileSource(type = TestCase.class,resources = {"/list_testCase.json", "/list_testCase.json"})
    void should_parse_multi_List_with_Integer_type_test_case(TestCase<List<Integer>,List<Integer>> testCase){
        assertEquals(1,testCase.input.get(0));
        assertEquals(5,testCase.output.get(2));
    }

    @ParameterizedTest
    @JsonFileSource(type = TestCase.class,resources = {"/map_testCase.json"})
    void should_parse_Map_type_test_case(TestCase<Map<String,Integer>,Map<String,Integer>> testCase){
        assertEquals(1,testCase.input.get("key1"));
        assertEquals(2,testCase.output.get("key2"));
    }

    @ParameterizedTest
    @JsonFileSource(type = TestCase.class,resources = {"map_testCase.json"})
    void should_parse_Map_type_test_case_with_direct_address(TestCase<Map<String,Integer>,Map<String,Integer>> testCase){
        assertEquals(1,testCase.input.get("key1"));
        assertEquals(2,testCase.output.get("key2"));
    }

    @Nested
    class defaultParseLackType{

        @ParameterizedTest
        @JsonFileSource(resources = {"list.json"})
        void should_parse_default_test_case(String testCase){
            List<People> peopleList = new TypeReference<List<People>>() {}.parseObject(testCase);
            assertEquals("lele",peopleList.get(0).name);
            assertEquals("02",peopleList.get(1).id);
        }

        public record People(String id,String name){}
    }
}