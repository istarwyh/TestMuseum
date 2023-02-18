package istarwyh.moduleloader.component;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class Node extends BaseDTO implements BoardModule<String>{

    private String data;


    public String getData(){
        return data;
    }

    @Override
    public void setData(Object data) {
        if(data instanceof String){
            this.setData((String)data);
        }
    }

    public void setData(String data) {
        this.data = data;
    }
}
