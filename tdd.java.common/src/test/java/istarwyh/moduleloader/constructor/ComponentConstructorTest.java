package istarwyh.moduleloader.constructor;

import com.alibaba.fastjson2.JSON;
import istarwyh.junit5.annotation.JsonFileSource;
import istarwyh.junit5.provider.model.TestCase;
import istarwyh.moduleloader.component.BaseElement;
import istarwyh.moduleloader.component.PageModule;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

import static com.alibaba.fastjson2.JSON.toJSONString;
import static istarwyh.moduleloader.display.ModuleLoader.DataContext;
import static istarwyh.moduleloader.display.ModuleLoader.createModuleLoader;
import static istarwyh.moduleloader.display.SubjectCodeEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ComponentConstructorTest {

    private DataContext context;

    @BeforeEach
    void setUp() {
        context = new DataContext();
        context.setBizCode(ABusiness.name());
    }

    @JsonFileSource(in = ComponentConstructorTest.class,resources = "component-business-constructor.json")
    void build(TestCase<Input,String> testCase) {
        Input input = testCase.getInput(Input.class);
        ViewStructure viewStructure = ViewStructure.of(input.getViewStructureStr());
        context.setElementMap(input.getElementMap());
        String output = testCase.getOutput(String.class);

        PageModule<?> build =  createModuleLoader(viewStructure, context).parse();

        assertEquals(output, toJSONString(build));
    }


    @Data
    private static class Input {

        private String viewStructureStr;

        private Map<String, BaseElement> elementMap;
    }

}