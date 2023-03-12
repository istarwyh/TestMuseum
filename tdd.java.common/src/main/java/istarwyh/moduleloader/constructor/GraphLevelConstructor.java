package istarwyh.moduleloader.constructor;


import istarwyh.moduleloader.DataContext;
import istarwyh.moduleloader.PageModuleConstructor;
import istarwyh.moduleloader.ViewStructure;
import istarwyh.moduleloader.BaseElement;
import istarwyh.moduleloader.component.GraphLevel;

public class GraphLevelConstructor implements PageModuleConstructor<GraphLevel<?>,Void> {
    @Override
    public GraphLevel<?> build(ViewStructure viewStructure, DataContext<Void> context) {
        GraphLevel<?> graphLevel = PageModuleConstructor.super.build(viewStructure, context);
        BaseElement baseElement = context.getElementMap().get(graphLevel.getSubjectCode());
        if(baseElement == null){
            return graphLevel;
        }
        graphLevel.setData(baseElement.getDetails());
        return graphLevel;
    }
}
