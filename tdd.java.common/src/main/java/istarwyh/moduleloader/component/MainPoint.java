package istarwyh.moduleloader.component;

import istarwyh.moduleloader.display.SubjectCodeEnum;
import org.apache.commons.collections.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class MainPoint extends BaseDTO implements BoardModule<List<BoardModule<?>>>{

    private  List<BoardModule<?>> data;

    private MainPoint(String subjectCode, List<BoardModule<?>> data) {
        super(subjectCode);
        this.data = data;
    }

    public static MainPoint createMainPoint(SubjectCodeEnum subjectCode, BoardModule<?>... data) {
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
    public List<BoardModule<?>> getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        if(data instanceof List) {
            this.setData((List<BoardModule<?>>) data);
        }
    }

    public void setData(List<BoardModule<?>> data) {
        this.data = data;
    }
}
