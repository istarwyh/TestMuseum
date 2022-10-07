package istarwyh.handler;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class MyOutputDTO{

    @Getter @Setter
    List<MyOutputDTO> outputs;

    public  record MyInputDTO() {}
}
