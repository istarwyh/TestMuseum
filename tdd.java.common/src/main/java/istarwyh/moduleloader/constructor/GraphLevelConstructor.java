package istarwyh.moduleloader.constructor;


import istarwyh.moduleloader.*;
import istarwyh.moduleloader.component.GraphLevel;

public class GraphLevelConstructor implements PageModuleConstructor<GraphLevel<?>,Void> {
    @Override
    public GraphLevel<?> construct(ViewStructure viewStructure, DataContext<Void> context) {
        GraphLevel<?> graphLevel = PageModuleConstructor.super.construct(viewStructure, context);
        ElementDTO baseElement = context.getElementMap().get(graphLevel.getSubjectCode());
        if(baseElement == null){
            return graphLevel;
        }
        graphLevel.setData(baseElement.getDetails());
        return graphLevel;
    }
}
