package istarwyh.moduleloader.component;

import istarwyh.moduleloader.constructor.BlockConstructor;
import istarwyh.moduleloader.display.SubjectCodeEnum;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;


public class Module extends BaseDTO implements BoardModule<List<Block>> {


    private List<Block> data;

    public Module(String subjectCode, List<Block> data) {
        super(subjectCode);
        this.data = data;
    }

    public static Module createModule(SubjectCodeEnum subjectCode, Block... data) {
        return new Module(subjectCode.name(), Arrays.stream(data).toList());
    }

    @Override
    public void setData(Object data) {
        if(data instanceof Block) {
            this.setData(List.of((Block) data));
        }
    }

    public void setData(List<Block> data) {
        this.data = data;
    }

    @Override
    public List<Block> getData() {
        return data;
    }
}
