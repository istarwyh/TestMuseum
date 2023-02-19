package istarwyh.moduleloader.constructor;

import com.alibaba.fastjson2.JSON;
import istarwyh.moduleloader.component.Node;
import lombok.Data;

@Data
public class ViewStructure {

    private String structureStr;

    private Node node;

    public ViewStructure(String structureStr) {
        if(structureStr.startsWith("[")){
            throw new IllegalArgumentException("board or module data must be a object instead of array");
        }
        this.structureStr = structureStr;
        this.node = JSON.parseObject(structureStr, Node.class);
    }

    public static ViewStructure of(String nodeData) {
        return new ViewStructure(nodeData);
    }

    public String getModuleTypeCode() {
        return node.getModuleTypeCode();
    }

}
