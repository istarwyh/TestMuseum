package istarwyh.moduleloader.component;

import istarwyh.moduleloader.AbstractElement;
import istarwyh.moduleloader.PageModule;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;


public class MainPoint extends AbstractElement<List<PageModule<?>>> {

    private  List<PageModule<?>> data;

    /**
     * 演示组件内的值会随着子元素的不同而动态变化
     * @return number
     */
    @Override
    public String getNumber() {
        if(CollectionUtils.isEmpty(data)){
            return super.getNumber();
        }
        return String.valueOf(data.size());
    }

    @Override
    public List<PageModule<?>> getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        if(data instanceof List) {
            this.setData((List<PageModule<?>>) data);
        }
    }

    public void setData(List<PageModule<?>> data) {
        this.data = data;
    }
}
