package istarwyh.page_module_loader;

import com.alibaba.fastjson2.JSONObject;
import istarwyh.junit5.annotation.JsonFileSource;
import istarwyh.junit5.provider.model.TestCase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViewStructureTest {

  @JsonFileSource(resources = "istarwyh/page_module_loader/component-business-constructor.json")
  void testExtractSubjectCodes(TestCase<JSONObject, List<String>> testCase) {
    List<String> list =
        ViewStructure.of(testCase.getInput()
                        .getString("viewStructureStr"))
            .extractSubjectCodes();
    assertEquals(10, list.size());
  }
}
