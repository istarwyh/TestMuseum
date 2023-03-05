package istarwyh.moduleloader.component;

import istarwyh.moduleloader.display.SubjectCodeEnum;

import java.util.Arrays;
import java.util.List;


public class Module extends BaseDTO implements PageModule<List<PageModule<?>>> {


    private List<PageModule<?>> data;

    public Module(String subjectCode, List<PageModule<?>> data) {
        super(subjectCode);
        this.data = data;
    }

    public static Module createModule(SubjectCodeEnum subjectCode, PageModule<?>... data) {
        return new Module(subjectCode.name(), Arrays.stream(data).toList());
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

    @Override
    public List<PageModule<?>> getData() {
        return data;
    }
}
