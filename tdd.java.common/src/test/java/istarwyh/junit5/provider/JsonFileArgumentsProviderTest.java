package istarwyh.junit5.provider;

import com.alibaba.fastjson2.TypeReference;
import istarwyh.junit5.annotation.JsonFileSource;
import istarwyh.junit5.provider.model.TestCase;
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


    @JsonFileSource(resources = {"istarwyh/absent/absent_test_case.json"})
    void should_generate_test_case_json_lack_of_it(TestCase<String,String> testCase){
        assertEquals("please input your case value", testCase.getInput());
        assertEquals("please input your case value", testCase.getOutput());
    }

    @JsonFileSource(in = JsonFileArgumentsProviderTest.class,resources = {"absent_test_case.json"})
    void should_generate_test_case_json_given_ownClass(TestCase<String,String> testCase){
        assertEquals("This is your input", testCase.getInput());
        assertEquals("This is your expected output", testCase.getOutput());
    }

    @JsonFileSource(in = JsonFileArgumentsProviderTest.class, resources = {"list_testCase.json", "list_testCase.json"})
    void should_parse_multi_List_with_Integer_type_test_case(TestCase<List<Integer>,List<Integer>> testCase){
        assertEquals(1,testCase.getInput().get(0));
        assertEquals(5,testCase.getOutput().get(2));
    }

    @JsonFileSource(resources = {"/istarwyh/junit5/provider/map_testCase.json"})
    void should_parse_Map_type_test_case(TestCase<Map<String,Integer>,Map<String,Integer>> testCase){
        assertEquals(1,testCase.getInput().get("key1"));
        assertEquals(2,testCase.getOutput().get("key2"));
    }

    @JsonFileSource(resources = {"istarwyh/junit5/provider/map_testCase.json"})
    void should_parse_Map_type_test_case_with_direct_address(TestCase<Map<String,Integer>,Map<String,Integer>> testCase){
        assertEquals(1,testCase.getInput().get("key1"));
        assertEquals(2,testCase.getOutput().get("key2"));
    }

    @JsonFileSource(resources = {"istarwyh/junit5/provider/people_list_testCase.json"})
    void should_parse_People_type_test_case_with_direct_address(TestCase<People,People> testCase){
        assertEquals("lele",testCase.getInput(People.class).name);
        assertEquals("lele",testCase.getInput(new TypeReference<>() {}).name);
        assertEquals("02",testCase.getOutput(People.class).id);
        assertEquals("02",testCase.getOutput(new TypeReference<>() {}).id);
    }

    @JsonFileSource(type = String.class,resources = {"istarwyh/junit5/provider/list.json"})
    void should_parse_String_input(String testCase){
        List<People> peopleList = new TypeReference<List<People>>() {}.parseObject(testCase);
        assertEquals("lele",peopleList.get(0).name);
        assertEquals("02",peopleList.get(1).id);
    }

    public record People(String id,String name){}

    // TODO get args type from test method's args and give the type to deserialization

}