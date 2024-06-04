package istarwyh.page_module_loader;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaohui
 */
@Data
public class ViewStructure {
  private String structureStr;

  private String moduleTypeCode;

  private ViewStructure(String structureStr) {
    if (structureStr.startsWith("[")) {
      throw new IllegalArgumentException("module data must be a object instead of array");
    }
    this.structureStr = structureStr;
    this.moduleTypeCode = JSON.parseObject(structureStr).get("moduleTypeCode").toString();
  }

  public static ViewStructure of(String structureStr) {
    return new ViewStructure(structureStr);
  }

  /**
   * @param str {@link String}
   * @return if {@link PageModule}
   */
  public static boolean isPageModuleStr(String str) {
    if (str == null) {
      return false;
    }
    return str.contains("moduleTypeCode");
  }

  public List<String> extractSubjectCodes(){
    List<String> subjectCodes = new ArrayList<>(10);
    findSubjectCodes(JSON.parseObject(this.getStructureStr()),subjectCodes);
    return subjectCodes;
  }

  private void findSubjectCodes(JSONObject jsonObject, List<String> subjectCodes) {
    for(String key : jsonObject.keySet()){
      Object value = jsonObject.get(key);
      if("subjectCode".equals(key) && value instanceof String){
        subjectCodes.add((String) value);
      }
      if(value instanceof JSONObject){
        findSubjectCodes((JSONObject) value,subjectCodes);
      }
      if(value instanceof JSONArray){
        for(Object element : (JSONArray)value){
          if(element instanceof JSONObject){
            findSubjectCodes((JSONObject) element,subjectCodes);
          }
        }
      }
    }
  }
}
