package istarwyh.page_module_loader;

import static com.alibaba.fastjson2.JSON.toJSONString;
import static istarwyh.page_module_loader.ModuleLoader.createModuleLoader;
import static istarwyh.page_module_loader.SubjectCodeEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import istarwyh.junit5.annotation.JsonFileSource;
import istarwyh.junit5.provider.model.TestCase;
import istarwyh.page_module_loader.component.AbstractElement;
import istarwyh.page_module_loader.component.Point;
import istarwyh.page_module_loader.constructor.MapBusinessConstructor;
import java.util.Map;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;

class ModuleLoaderTest {

    private DataContext<AbstractElement<?>,MapBusinessConstructor.QueryDTO> context;

    @BeforeEach
    void setUp() {
        context = new DataContext<>();
        context.setQueryDTO(MapBusinessConstructor.QueryDTO.builder().bizCode(ABusiness.name()).build());
    }

    @JsonFileSource(resources = "component-business-constructor.json")
    void build(TestCase<Input,String> testCase) {
        Input input = testCase.getInput(Input.class);
        ViewStructure viewStructure = ViewStructure.of(input.getViewStructureStr());
        Map<String, Point> map = input.getElementMap();
        context.setElementMap(map);

        PageModule<?> build =  createModuleLoader(viewStructure, context).parse();

        assertEquals(testCase.getOutput(String.class), toJSONString(build));
    }


    @Data
    private static class Input {

        private String viewStructureStr;

        private Map<String, Point> elementMap;
    }

}