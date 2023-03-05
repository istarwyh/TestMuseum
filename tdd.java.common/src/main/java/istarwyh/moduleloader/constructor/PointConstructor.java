package istarwyh.moduleloader.constructor;

import com.alibaba.fastjson2.JSON;
import istarwyh.moduleloader.component.BaseDTO;
import istarwyh.moduleloader.component.Point;
import istarwyh.moduleloader.display.ModuleLoader;

import java.util.Map;

public class PointConstructor implements ComponentConstructor<Point> {

    public static PointConstructor empty() {
        return new PointConstructor();
    }

    @Override
    public Point build(ViewStructure viewStructure, ModuleLoader.DataContext context) {
        Point point = JSON.parseObject(viewStructure.getStructureStr(), Point.class);
        BaseDTO baseDTO = context.getElementMap().get(point.getSubjectCode());
        point.setAmount(baseDTO.getAmount());
        point.setNumber(baseDTO.getNumber());
        return point;
    }
}
