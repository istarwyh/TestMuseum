package istarwyh.moduleloader.constructor;

import com.alibaba.fastjson2.JSON;
import istarwyh.moduleloader.component.Block;
import istarwyh.moduleloader.component.MainPoint;

public class MainPointConstructor implements ComponentConstructor<MainPoint> {

    private MainPointConstructor(ViewStructure viewStructure) {
    }

    public static MainPointConstructor createMainPointConstructor(ViewStructure viewStructure) {
        return new MainPointConstructor(viewStructure);
    }
}
