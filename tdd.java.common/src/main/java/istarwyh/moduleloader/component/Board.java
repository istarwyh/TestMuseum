package istarwyh.moduleloader.component;

import lombok.Data;
import lombok.Setter;

import java.util.List;


public class Board extends BaseDTO implements BoardModule<List<Module>> {

    private List<Module> data;

    private Board(String subjectCode, List<Module> data) {
        super(subjectCode);
        this.data = data;
    }

    public static Board createBoard(String subjectCode, List<Module> data) {
        return new Board(subjectCode, data);
    }

    @Override
    public List<Module> getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        if(data instanceof List) {
            this.setData((List<Module>) data);
        }
    }

    public void setData(List<Module> data) {
        this.data = data;
    }
}
