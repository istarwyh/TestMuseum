package istarwyh.moduleloader.constructor;


import istarwyh.moduleloader.component.GraphLevel;
import istarwyh.moduleloader.component.Module;

public class GraphLevelConstructor implements ComponentConstructor<GraphLevel<?>> {

    public static GraphLevelConstructor empty() {
        return new GraphLevelConstructor();
    }
}
