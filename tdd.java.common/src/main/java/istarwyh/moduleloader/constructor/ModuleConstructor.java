package istarwyh.moduleloader.constructor;


import istarwyh.moduleloader.component.Module;

public class ModuleConstructor implements PageModuleConstructor<Module> {

    public static ModuleConstructor empty() {
        return new ModuleConstructor();
    }
}
