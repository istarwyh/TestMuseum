package istarwyh.moduleloader.constructor;

import com.alibaba.fastjson2.JSON;
import istarwyh.moduleloader.component.BaseDTO;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class ViewStructure {

    private String structureStr;

    private Structure structure;

    private ViewStructure(String structureStr) {
        if(structureStr.startsWith("[")){
            throw new IllegalArgumentException("board or module data must be a object instead of array");
        }
        this.structureStr = structureStr;
        this.structure = JSON.parseObject(structureStr, Structure.class);
    }

    public static ViewStructure of(String structureStr) {
        return new ViewStructure(structureStr);
    }

    public String getModuleTypeCode() {
        return structure.getModuleTypeCode();
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class Structure extends BaseDTO {

        private String data;
    }

}
