package istarwyh.moduleloader.constructor;


import com.alibaba.fastjson2.JSON;
import istarwyh.moduleloader.component.Module;

public class ModuleConstructor implements ComponentConstructor<Module> {

    private ModuleConstructor() {}

    public static ModuleConstructor empty(ViewStructure viewStructure) {
        return new ModuleConstructor();
    }
}
