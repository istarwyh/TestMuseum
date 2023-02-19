package istarwyh.moduleloader.component;

import java.util.List;


public class Board extends BaseDTO implements BoardModule<List<BoardModule<?>>> {

    private List<BoardModule<?>> data;

    private Board(String subjectCode, List<BoardModule<?>> data) {
        super(subjectCode);
        this.data = data;
    }

    public static Board createBoard(String subjectCode, List<BoardModule<?>> data) {
        return new Board(subjectCode, data);
    }

    @Override
    public List<BoardModule<?>> getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        if(data instanceof List) {
            this.setData(data);
        }
    }

    public void setData(List<BoardModule<?>> data) {
        this.data = data;
    }
}
