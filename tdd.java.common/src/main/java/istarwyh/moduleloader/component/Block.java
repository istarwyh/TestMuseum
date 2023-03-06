package istarwyh.moduleloader.component;

import istarwyh.moduleloader.display.SubjectCodeEnum;

import java.util.Arrays;
import java.util.List;


public class Block extends BaseElement implements PageModule<List<PageModule<?>>> {

    private  List<PageModule<?>> data;

    private Block(String subjectCode, List<PageModule<?>> data) {
        super();
        super.setSubjectCode(subjectCode);
        this.data = data;
    }

    public static Block createBlock(SubjectCodeEnum subjectCode, PageModule<?>... data) {
        return new Block(subjectCode.name(), Arrays.stream(data).toList());
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
