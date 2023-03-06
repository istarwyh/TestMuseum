package istarwyh.moduleloader.component;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Point extends BaseElement implements PageModule<Void>{


    @Override
    public Void getData() {
        return null;
    }

    @Override
    public void setData(Object data) {

    }
}
