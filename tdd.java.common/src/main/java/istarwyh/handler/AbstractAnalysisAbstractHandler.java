package istarwyh.handler;

import java.util.ArrayList;

import static istarwyh.handler.ContextHolder.*;
import static istarwyh.handler.MyOutputDTO.*;

/**
 * @author xiaohui
 */
public abstract class AbstractAnalysisAbstractHandler extends AbstractHandler<MyOutputDTO> {

    protected MyInputDTO myInputDTO;

    protected MyOutputDTO outputDTO;

    public AbstractAnalysisAbstractHandler() {
        this.myInputDTO = getMyInput();
        if(getMyOutput() == null){
            outputDTO = new MyOutputDTO().setOutputs(new ArrayList<>());
            setMyOutput(outputDTO);
        }else {
            outputDTO = getMyOutput();
        }
    }
}
