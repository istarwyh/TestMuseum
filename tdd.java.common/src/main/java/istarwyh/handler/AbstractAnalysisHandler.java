package istarwyh.handler;

import java.util.ArrayList;

import static istarwyh.handler.ContextHolder.*;
import static istarwyh.handler.MyOutputDTO.*;

/**
 * @author xiaohui
 */
public abstract class AbstractAnalysisHandler extends Handler<MyOutputDTO>{

    MyInputDTO myInputDTO;

    MyOutputDTO outputDTO;

    public AbstractAnalysisHandler() {
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
