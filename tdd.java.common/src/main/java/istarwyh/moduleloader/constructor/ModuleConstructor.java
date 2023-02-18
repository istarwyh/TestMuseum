package istarwyh.moduleloader.constructor;


import java.lang.reflect.Type;
import java.util.List;

import com.alibaba.fastjson2.JSON;
import istarwyh.moduleloader.component.Module;
import istarwyh.moduleloader.component.Node;

public class ModuleConstructor implements BoardConstructor<Module>{

    private final ViewStructure viewStructure;


    private ModuleConstructor(ViewStructure viewStructure) {
        this.viewStructure = viewStructure;
    }

    public static ModuleConstructor createModuleConstructor(ViewStructure viewStructure) {
        return new ModuleConstructor(viewStructure);
    }


    public Module build(ViewStructure viewStructure, Object queryDTO) {
        String structureStr = viewStructure.getStructureStr();
        return JSON.parseObject(structureStr, this.support());
    }
}
