package io.github.istarwyh.junit5.provider;

import static org.junit.jupiter.api.Assertions.*;

import com.alibaba.fastjson2.TypeReference;
import io.github.istarwyh.junit5.annotation.JsonFileSource;
import io.github.istarwyh.junit5.provider.model.TestCase;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class JsonFileArgumentsProviderTest {

    @ParameterizedTest
    @CsvSource(value = {"1,2", "3,4"})
    void should_show_how_to_parse_multi_args_with_csv(Integer in, Integer out) {
        assertEquals(out, in + 1);
    }

    @JsonFileSource(resources = {"absent_test_case.json"})
    void should_generate_test_case_json_lack_of_it(TestCase<String, String> testCase) {
        assertEquals("eOMtThyhVNLWUZNRcBaQKxI", testCase.getInput());
        assertEquals("yedUsFwdkelQbxeTeQOvaScfqIOOmaa", testCase.getOutput());
    }

    @JsonFileSource(resources = {"absent_test_case.json"})
    void should_generate_test_case_json_given_ownClass(TestCase<String, String> testCase) {
        assertEquals("eOMtThyhVNLWUZNRcBaQKxI", testCase.getInput());
        assertEquals("yedUsFwdkelQbxeTeQOvaScfqIOOmaa", testCase.getOutput());
    }

    @JsonFileSource(resources = {"list_testCase.json", "list_testCase.json"})
    void should_parse_multi_List_with_Integer_type_test_case(
            TestCase<List<Integer>, List<Integer>> testCase) {
        assertEquals(1, testCase.getInput().get(0));
        assertEquals(5, testCase.getOutput().get(2));
    }

    @JsonFileSource(resources = {"map_testCase.json"})
    void should_parse_Map_type_test_case(
            TestCase<Map<String, Integer>, Map<String, Integer>> testCase) {
        assertEquals(1, testCase.getInput().get("key1"));
        assertEquals(2, testCase.getOutput().get("key2"));
    }

    @JsonFileSource(resources = {"map_testCase.json"})
    void should_parse_Map_type_test_case_with_direct_address(
            TestCase<Map<String, Integer>, Map<String, Integer>> testCase) {
        assertEquals(1, testCase.getInput().get("key1"));
        assertEquals(2, testCase.getOutput().get("key2"));
    }

    @JsonFileSource(resources = {"people_list_testCase.json"})
    void should_parse_People_type_test_case_with_direct_address(TestCase<People, People> testCase) {
        assertEquals("lele", testCase.getInput(People.class).name);
        assertEquals("lele", testCase.getInput(new TypeReference<People>() {}).name);
        assertEquals("02", testCase.getOutput(People.class).id);
        assertEquals("02", testCase.getOutput(new TypeReference<People>() {}).id);
    }

    @JsonFileSource(resources = {"list.json"})
    void should_parse_String_input(String testCase) {
        List<People> peopleList = new TypeReference<List<People>>() {}.parseObject(testCase);
        assertEquals("lele", peopleList.get(0).name);
        assertEquals("02", peopleList.get(1).id);
    }

    @JsonFileSource(resources = {"people_input.json"})
    void should_parse_People_input(People people) {
        assertEquals("lele", people.name);
    }

    @JsonFileSource(resources = {"RecursionClass_input.json"})
    void should_parse_recursionClass_input(RecursionClass recursionClass) {
        assertNotNull(recursionClass);
        assertNull(recursionClass.getRecursionClasses());
        assertNotNull(recursionClass.getPeople());
    }

    @Test
    void setNullIfRecursive() {
        RecursionClass recursionClass = new EasyRandom().nextObject(RecursionClass.class);
        JsonFileArgumentsProvider.setNullIfRecursive(recursionClass);
        assertNull(recursionClass.getRecursionClass());
        assertNull(recursionClass.getRecursionClasses());
        assertNotNull(recursionClass.getPeople());
    }

    @Data
    public static class RecursionClass {
        private People people;
        private RecursionClass recursionClass;
        private List<RecursionClass> recursionClasses;
    }

    public static class People {
        public final String id = "02";
        public final String name = "lele";
    }

    // TODO get args type from test method's args and give the type to deserialization

}
