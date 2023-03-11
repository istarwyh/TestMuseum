package istarwyh.moduleloader.component;

import istarwyh.moduleloader.PageModule;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class AnnularChart extends BaseElement implements PageModule<List<GraphLevel<Point>>> {

    private List<GraphLevel<Point>> data;

    @Override
    public List<GraphLevel<Point>> getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        if(data instanceof List){
            this.data = (List<GraphLevel<Point>>) data;
        }
    }
}
