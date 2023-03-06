package istarwyh.moduleloader.constructor;

import istarwyh.moduleloader.component.MainPoint;

public class MainPointConstructor implements PageModuleConstructor<MainPoint> {

    public static MainPointConstructor empty() {
        return new MainPointConstructor();
    }
}
