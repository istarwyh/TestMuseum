package istarwyh.moduleloader.constructor;

import istarwyh.junit5.annotation.JsonFileSource;
import istarwyh.junit5.provider.model.TestCase;
import istarwyh.moduleloader.component.BoardModule;
import istarwyh.moduleloader.display.ModuleLoader;
import istarwyh.moduleloader.display.SubjectCodeEnum;

import static com.alibaba.fastjson2.JSON.toJSONString;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MapBusinessConstructorTest {

    @JsonFileSource(in = MapBusinessConstructorTest.class,resources = "map-business-constructor.json")
    void build(TestCase<String,String> testCase) {
        String input = testCase.getInput(String.class);
        ViewStructure viewStructure = new ViewStructure(input);
        String output = testCase.getOutput(String.class);

        BoardModule<?> build =  ModuleLoader.createModuleLoader(viewStructure, SubjectCodeEnum.ABusiness.name()).parse();

        assertEquals(output, toJSONString(build));
    }

}