package istarwyh.provider;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import istarwyh.junit5.annotation.JsonFileSource;
import istarwyh.provider.model.TestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonFileArgumentsProviderTest {

    @ParameterizedTest
    @JsonFileSource(type = TestCase.class,resources = {"/list.json"})
    void should_parse_List_type_test_case(TestCase<List<Integer>,List<Integer>> testCase){
        assertEquals("[1,2,3]",testCase.input.toString());
        assertEquals("[3,4,5]",testCase.output.toString());
    }

    @Test
    void should_1(){
        TypeReference<TestCase<List<Integer>, List<Integer>>> typeReference = new TypeReference<>() {};
        var testCase = typeReference.parseObject("{\"in\":[1,2,3],'out':[3,4,5]}");
        System.out.println(testCase.input.toString());
        System.out.println(testCase.output.toString());
        String s = JSON.toJSONString(typeReference);
        System.out.println(s);
        JSONObject newType = JSON.parseObject(s);
        
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1,2",
            "3,4"
    })
    void should_csv(Integer in,Integer out){
        System.out.println(in.toString());
        System.out.println(out.toString());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1,2",
            "3,4"
    })
    void should_csv2(Integer in){
        System.out.println(in.toString());
    }
}