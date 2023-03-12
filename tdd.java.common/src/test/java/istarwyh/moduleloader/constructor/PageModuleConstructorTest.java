package istarwyh.moduleloader.constructor;

import istarwyh.junit5.annotation.JsonFileSource;
import istarwyh.junit5.provider.model.TestCase;
import istarwyh.moduleloader.ViewStructure;
import istarwyh.moduleloader.component.BaseElement;
import istarwyh.moduleloader.PageModule;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;

import static com.alibaba.fastjson2.JSON.toJSONString;
import static istarwyh.moduleloader.ModuleLoader.DataContext;
import static istarwyh.moduleloader.ModuleLoader.createModuleLoader;
import static istarwyh.moduleloader.SubjectCodeEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PageModuleConstructorTest {

    private DataContext<MapBusinessConstructor.QueryDTO> context;

    @BeforeEach
    void setUp() {
        context = new DataContext<>();
        context.setQueryDTO(MapBusinessConstructor.QueryDTO.builder().bizCode(ABusiness.name()).build());
    }

    @JsonFileSource(in = PageModuleConstructorTest.class,resources = "component-business-constructor.json")
    void build(TestCase<Input,String> testCase) {
        Input input = testCase.getInput(Input.class);
        ViewStructure viewStructure = ViewStructure.of(input.getViewStructureStr());
        context.setElementMap(input.getElementMap());

        PageModule<?> build =  createModuleLoader(viewStructure, context).parse();

        assertEquals(testCase.getOutput(String.class), toJSONString(build));
    }


    @Data
    private static class Input {

        private String viewStructureStr;

        private Map<String, BaseElement> elementMap;
    }

}