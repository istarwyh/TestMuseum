package istarwyh.moduleloader.constructor;


import istarwyh.moduleloader.PageModuleConstructor;
import istarwyh.moduleloader.component.Module;

public class ModuleConstructor implements PageModuleConstructor<Module,Void> {

    public static ModuleConstructor empty() {
        return new ModuleConstructor();
    }
}
