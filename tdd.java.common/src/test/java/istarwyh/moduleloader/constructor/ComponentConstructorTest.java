package istarwyh.moduleloader.constructor;

import istarwyh.junit5.annotation.JsonFileSource;
import istarwyh.junit5.provider.model.TestCase;
import istarwyh.moduleloader.component.BaseDTO;
import istarwyh.moduleloader.component.PageModule;
import istarwyh.moduleloader.component.Point;
import istarwyh.moduleloader.display.ModuleLoader;
import istarwyh.moduleloader.display.SubjectCodeEnum;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;

import static com.alibaba.fastjson2.JSON.toJSONString;
import static istarwyh.moduleloader.display.ModuleLoader.*;
import static istarwyh.moduleloader.display.ModuleLoader.createModuleLoader;
import static istarwyh.moduleloader.display.SubjectCodeEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ComponentConstructorTest {

    private DataContext context;

    @BeforeEach
    void setUp() {
        context = new DataContext();
        context.setBizCode(ABusiness.name());

        HashMap<String, BaseDTO> elementMap = new HashMap<>();
        context.setElementMap(elementMap);
        elementMap.put(___ALL_SETTLE_SALE.name(), BaseDTO.of(___ALL_SETTLE_SALE,"1.00","2"));
        elementMap.put(___ALL_SETTLE_REFUND.name(),BaseDTO.of(___ALL_SETTLE_REFUND,"2.00","3"));
        elementMap.put(___NEW_ACTUAL_SALE.name(), BaseDTO.of(___NEW_ACTUAL_SALE,"3.00","4"));
        elementMap.put(___OLD_ACTUAL_REFUND.name(),BaseDTO.of(___OLD_ACTUAL_REFUND,"4.00","5"));
        elementMap.put(___CONSIGNMENT_ACTUAL_SALE.name(), BaseDTO.of(___CONSIGNMENT_ACTUAL_SALE,"5.00","6"));
    }

    @JsonFileSource(in = ComponentConstructorTest.class,resources = "component-business-constructor.json")
    void build(TestCase<String,String> testCase) {
        String input = testCase.getInput(String.class);
        ViewStructure viewStructure = ViewStructure.of(input);
        String output = testCase.getOutput(String.class);

        PageModule<?> build =  createModuleLoader(viewStructure, context).parse();

        assertEquals(output, toJSONString(build));
    }

}