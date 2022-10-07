package istarwyh.handler;

import java.util.ArrayList;

import static istarwyh.handler.ContextHolder.*;
import static istarwyh.handler.MyOutputDTO.*;

public abstract class AnalysisHandler extends Handler<MyOutputDTO>{

    MyInputDTO myInputDTO;

    MyOutputDTO outputDTO;

    public AnalysisHandler() {
        this.myInputDTO = getMyInput();
        if(getMyOutput() == null){
            outputDTO = new MyOutputDTO();
            outputDTO.setOutputs(new ArrayList<>());
            setMyOutput(outputDTO);
        }else {
            outputDTO = getMyOutput();
        }
    }
}
