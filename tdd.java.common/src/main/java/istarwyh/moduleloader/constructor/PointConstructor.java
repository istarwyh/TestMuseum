package istarwyh.moduleloader.constructor;

import com.alibaba.fastjson2.JSON;
import istarwyh.moduleloader.DataContext;
import istarwyh.moduleloader.PageModuleConstructor;
import istarwyh.moduleloader.ViewStructure;
import istarwyh.moduleloader.BaseElement;
import istarwyh.moduleloader.component.Point;

public class PointConstructor implements PageModuleConstructor<Point,Void> {

    @Override
    public Point build(ViewStructure viewStructure, DataContext<Void> context) {
        Point point = JSON.parseObject(viewStructure.getStructureStr(), Point.class);
        BaseElement baseElement = context.getElementMap().get(point.getSubjectCode());
        if(baseElement == null) {
            return point;
        }
        point.setAmount(baseElement.getAmount());
        point.setNumber(baseElement.getNumber());
        return point;
    }
}
