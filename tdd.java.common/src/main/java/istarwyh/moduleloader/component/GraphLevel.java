package istarwyh.moduleloader.component;

import istarwyh.moduleloader.display.SubjectCodeEnum;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class GraphLevel<T> extends BaseDTO implements PageModule<List<T>> {


    private List<T> data;

    public GraphLevel(String subjectCode, List<T> data) {
        super(subjectCode);
        this.data = data;
    }

    @SafeVarargs
    public static <T> GraphLevel<T> createModule(SubjectCodeEnum subjectCode, T... data) {
        return new GraphLevel<>(subjectCode.name(), Arrays.stream(data).toList());
    }

    @Override
    public void setData(Object data) {
        if(data instanceof List) {
            this.setData((List<T>) data);
        }
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public List<T> getData() {
        return data;
    }
}
