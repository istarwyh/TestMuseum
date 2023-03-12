package istarwyh.moduleloader.component;

import istarwyh.moduleloader.AbstractElement;
import istarwyh.moduleloader.PageModule;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;


public class MainPoint extends AbstractElement<List<PageModule<?>>> {
    /**
     * 演示组件内的值会随着子元素的不同而动态变化
     * @return number
     */
    @Override
    public String getNumber() {
        if(CollectionUtils.isEmpty(super.getData())){
            return super.getNumber();
        }
        return String.valueOf(super.getData().size());
    }
}
