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
    @JsonFileSource(type = TestCase.class,resources = {"/istarwyh.provider/list_testCase.json"})
    void should_parse_List_type_test_case(TestCase<List<Integer>,List<Integer>> testCase){
        assertEquals("[1,2,3]",testCase.getInput().toString());
        assertEquals("[3,4,5]",testCase.getOutput().toString());
    }

    @ParameterizedTest
    @JsonFileSource(type = TestCase.class,resources = {"/istarwyh.provider/list_testCase.json", "/istarwyh.provider/list_testCase.json"})
    void should_parse_multi_List_with_Integer_type_test_case(TestCase<List<Integer>,List<Integer>> testCase){
        assertEquals(1,testCase.getInput().get(0));
        assertEquals(5,testCase.getOutput().get(2));
    }

    @ParameterizedTest
    @JsonFileSource(type = TestCase.class,resources = {"/istarwyh.provider/map_testCase.json"})
    void should_parse_Map_type_test_case(TestCase<Map<String,Integer>,Map<String,Integer>> testCase){
        assertEquals(1,testCase.getInput().get("key1"));
        assertEquals(2,testCase.getOutput().get("key2"));
    }

    @ParameterizedTest
    @JsonFileSource(type = TestCase.class,resources = {"istarwyh.provider/map_testCase.json"})
    void should_parse_Map_type_test_case_with_direct_address(TestCase<Map<String,Integer>,Map<String,Integer>> testCase){
        assertEquals(1,testCase.getInput().get("key1"));
        assertEquals(2,testCase.getOutput().get("key2"));
    }

    @ParameterizedTest
    @JsonFileSource(type = TestCase.class,resources = {"istarwyh.provider/people_list_testCase.json"})
    void should_parse_People_type_test_case_with_direct_address(TestCase<People,People> testCase){
        assertEquals("lele",testCase.getInput(People.class).name);
        assertEquals("lele",testCase.getInput(new TypeReference<>() {}).name);
        assertEquals("02",testCase.getOutput(People.class).id);
        assertEquals("02",testCase.getOutput(new TypeReference<>() {}).id);
    }

    @Nested
    class defaultParseLackType{

        @ParameterizedTest
        @JsonFileSource(resources = {"istarwyh.provider/list.json"})
        void should_parse_default_test_case(String testCase){
            List<People> peopleList = new TypeReference<List<People>>() {}.parseObject(testCase);
            assertEquals("lele",peopleList.get(0).name);
            assertEquals("02",peopleList.get(1).id);
        }
    }
    public record People(String id,String name){}

    // TODO get args type from test method's args and give the type to deserialization

}