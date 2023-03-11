package istarwyh.moduleloader.constructor;

import istarwyh.moduleloader.PageModuleConstructor;
import istarwyh.moduleloader.component.MainPoint;

public class MainPointConstructor implements PageModuleConstructor<MainPoint,Void> {

    public static MainPointConstructor empty() {
        return new MainPointConstructor();
    }
}
