package istarwyh.moduleloader.component;

import istarwyh.moduleloader.BaseElement;
import istarwyh.moduleloader.PageModule;
import istarwyh.moduleloader.SubjectCodeEnum;

import java.util.Arrays;
import java.util.List;


public class Block extends BaseElement implements PageModule<List<PageModule<?>>> {

    private  List<PageModule<?>> data;

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
