package istarwyh.moduleloader;

import com.alibaba.fastjson2.JSON;
import lombok.Data;

@Data
public class ViewStructure {
    private String structureStr;
    private String moduleTypeCode;

    private ViewStructure(String structureStr) {
        if(structureStr.startsWith("[")){
            throw new IllegalArgumentException("module data must be a object instead of array");
        }
        this.structureStr = structureStr;
        this.moduleTypeCode = JSON.parseObject(structureStr).get("moduleTypeCode").toString();
    }

    public static ViewStructure of(String structureStr) {
        return new ViewStructure(structureStr);
    }

    /**
     *
     * @param str {@link String}
     * @return if {@link PageModule}
     */
    public static boolean isPageModuleStr(String str) {
        if(str == null) {
            return false;
        }
        return str.contains("moduleTypeCode");
    }

    public String getModuleTypeCode() {
        return moduleTypeCode;
    }

}
