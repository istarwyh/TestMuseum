package istarwyh.moduleloader.component;

import istarwyh.moduleloader.AbstractElement;
import istarwyh.moduleloader.PageModule;

import java.util.List;


public class Module extends AbstractElement<List<PageModule<?>>> {


    private List<PageModule<?>> data;

    @Override
    public void setData(Object data) {
        if(data instanceof List) {
            this.setData((List<PageModule<?>>) data);
        }
    }

    public void setData(List<PageModule<?>> data) {
        this.data = data;
    }

    @Override
    public List<PageModule<?>> getData() {
        return data;
    }
}
