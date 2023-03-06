package istarwyh.moduleloader.constructor;


import istarwyh.moduleloader.component.BaseElement;
import istarwyh.moduleloader.component.GraphLevel;
import istarwyh.moduleloader.display.ModuleLoader;

public class GraphLevelConstructor implements ComponentConstructor<GraphLevel<?>> {

    public static GraphLevelConstructor empty() {
        return new GraphLevelConstructor();
    }

    @Override
    public GraphLevel<?> build(ViewStructure viewStructure, ModuleLoader.DataContext context) {
        GraphLevel<?> graphLevel = ComponentConstructor.super.build(viewStructure, context);
        BaseElement baseElement = context.getElementMap().get(graphLevel.getSubjectCode());
        if(baseElement == null){
            return graphLevel;
        }
        graphLevel.setData(baseElement.getDetails());
        return graphLevel;
    }
}
