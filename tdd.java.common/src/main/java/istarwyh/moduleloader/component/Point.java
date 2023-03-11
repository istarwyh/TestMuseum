package istarwyh.moduleloader.component;

import istarwyh.moduleloader.PageModule;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Point extends BaseElement implements PageModule<Void> {


    @Override
    public Void getData() {
        return null;
    }

    @Override
    public void setData(Object data) {

    }
}
