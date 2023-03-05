package istarwyh.moduleloader.component;

import java.util.List;


public class Page extends BaseDTO implements PageModule<List<PageModule<?>>> {

    private List<PageModule<?>> data;

    private Page(String subjectCode, List<PageModule<?>> data) {
        super(subjectCode);
        this.data = data;
    }

    public static Page createBoard(String subjectCode, List<PageModule<?>> data) {
        return new Page(subjectCode, data);
    }

    @Override
    public List<PageModule<?>> getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        if(data instanceof List) {
            this.setData(data);
        }
    }

    public void setData(List<PageModule<?>> data) {
        this.data = data;
    }
}
