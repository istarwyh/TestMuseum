package istarwyh.moduleloader.component;

import istarwyh.moduleloader.display.SubjectCodeEnum;

import java.util.Arrays;
import java.util.List;


public class Block extends BaseDTO implements BoardModule<List<BoardModule<?>>>{

    private  List<BoardModule<?>> data;

    private Block(String subjectCode, List<BoardModule<?>> data) {
        super(subjectCode);
        this.data = data;
    }

    public static Block createBlock(SubjectCodeEnum subjectCode, BoardModule<?>... data) {
        return new Block(subjectCode.name(), Arrays.stream(data).toList());
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
