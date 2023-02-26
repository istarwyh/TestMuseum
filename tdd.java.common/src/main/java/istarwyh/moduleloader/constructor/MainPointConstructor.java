package istarwyh.moduleloader.constructor;

import istarwyh.moduleloader.component.MainPoint;

public class MainPointConstructor implements ComponentConstructor<MainPoint> {

    public static MainPointConstructor empty() {
        return new MainPointConstructor();
    }
}
