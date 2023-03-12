package istarwyh.moduleloader.component;

import istarwyh.moduleloader.AbstractElement;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class BarChart extends AbstractElement<List<GraphLevel<Point>>> {

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
