package istarwyh.moduleloader.component;

import istarwyh.moduleloader.PageModule;
import istarwyh.moduleloader.SubjectCodeEnum;
import org.apache.commons.collections.CollectionUtils;

import java.util.Arrays;
import java.util.List;


public class MainPoint extends BaseElement implements PageModule<List<PageModule<?>>> {

    private  List<PageModule<?>> data;

    private MainPoint(String subjectCode, List<PageModule<?>> data) {
        super();
        super.setSubjectCode(subjectCode);
        this.data = data;
    }

    public static MainPoint createMainPoint(SubjectCodeEnum subjectCode, PageModule<?>... data) {
        return new MainPoint(subjectCode.name(), Arrays.stream(data).toList());
    }

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
    public String getSubjectCode() {
        return super.getSubjectCode();
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
