package istarwyh.page_module_loader;

import istarwyh.junit5.annotation.JsonFileSource;
import istarwyh.junit5.provider.model.TestCase;
import istarwyh.page_module_loader.bill.BillElementDTO;
import istarwyh.page_module_loader.constructor.MapBusinessConstructor;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;

import static com.alibaba.fastjson2.JSON.toJSONString;

import static istarwyh.page_module_loader.PageModuleLoader.createModuleLoader;
import static istarwyh.page_module_loader.SubjectCodeEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PageModuleLoaderTest {

    private DataContext<MapBusinessConstructor.QueryDTO> context;

    @BeforeEach
    void setUp() {
        context = new DataContext<>();
        context.setQueryDTO(MapBusinessConstructor.QueryDTO.builder().bizCode(ABusiness.name()).build());
    }

    @JsonFileSource(resources = "component-business-constructor.json")
    void build(TestCase<Input,String> testCase) {
        Input input = testCase.getInput(Input.class);
        PageModuleRawStructure pageModuleRawStructure = PageModuleRawStructure.of(input.getViewStructureStr());
        context.setElementMap(input.getElementMap());

        PageModule<?> build =  createModuleLoader(pageModuleRawStructure, context).parse();

        assertEquals(testCase.getOutput(String.class), toJSONString(build));
    }


    @Data
    private static class Input {

        private String viewStructureStr;

        private Map<String, BillElementDTO> elementMap;
    }

}