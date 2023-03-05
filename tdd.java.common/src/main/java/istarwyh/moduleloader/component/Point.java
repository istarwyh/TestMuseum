package istarwyh.moduleloader.component;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Point extends BaseDTO implements PageModule<Void>{

    public Point(String subjectCode) {
        super(subjectCode);
    }


    @Override
    public Void getData() {
        return null;
    }

    @Override
    public void setData(Object data) {

    }
}
