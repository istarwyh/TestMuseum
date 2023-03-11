package istarwyh.moduleloader.constructor;


import istarwyh.moduleloader.PageModuleConstructor;
import istarwyh.moduleloader.component.BaseElement;
import istarwyh.moduleloader.component.GraphLevel;
import istarwyh.moduleloader.ModuleLoader;

public class GraphLevelConstructor implements PageModuleConstructor<GraphLevel<?>,Void> {

    public static GraphLevelConstructor empty() {
        return new GraphLevelConstructor();
    }

    @Override
    public GraphLevel<?> build(ViewStructure viewStructure, ModuleLoader.DataContext<Void> context) {
        GraphLevel<?> graphLevel = PageModuleConstructor.super.build(viewStructure, context);
        BaseElement baseElement = context.getElementMap().get(graphLevel.getSubjectCode());
        if(baseElement == null){
            return graphLevel;
        }
        graphLevel.setData(baseElement.getDetails());
        return graphLevel;
    }
}
