package istarwyh.moduleloader.component;

import istarwyh.moduleloader.constructor.BlockConstructor;
import istarwyh.moduleloader.display.SubjectCodeEnum;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;


public class Module extends BaseDTO implements BoardModule<List<BoardModule<?>>> {


    private List<BoardModule<?>> data;

    public Module(String subjectCode, List<BoardModule<?>> data) {
        super(subjectCode);
        this.data = data;
    }

    public static Module createModule(SubjectCodeEnum subjectCode, BoardModule<?>... data) {
        return new Module(subjectCode.name(), Arrays.stream(data).toList());
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

    @Override
    public List<BoardModule<?>> getData() {
        return data;
    }
}
