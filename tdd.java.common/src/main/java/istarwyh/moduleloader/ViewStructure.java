package istarwyh.moduleloader;

import com.alibaba.fastjson2.JSON;
import lombok.Data;

@Data
public class ViewStructure {
    private String structureStr;
    private String moduleTypeCode;

    private ViewStructure(String structureStr) {
        if(structureStr.startsWith("[")){
            throw new IllegalArgumentException("board or module data must be a object instead of array");
        }
        this.structureStr = structureStr;
        this.moduleTypeCode = JSON.parseObject(structureStr).get("moduleTypeCode").toString();;
    }

    public static ViewStructure of(String structureStr) {
        return new ViewStructure(structureStr);
    }

    public String getModuleTypeCode() {
        return moduleTypeCode;
    }

}
