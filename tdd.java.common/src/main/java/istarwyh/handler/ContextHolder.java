package istarwyh.handler;

/**
 * @author xiaohui
 */
public class ContextHolder {

    private static final ThreadLocal<MyOutputDTO> MY_OUTPUT_THREAD_LOCAL = new ThreadLocal<>();

    private static final ThreadLocal<MyOutputDTO.MyInputDTO> MY_INPUT_THREAD_LOCAL = new ThreadLocal<>();


    public static MyOutputDTO getMyOutput(){
        return MY_OUTPUT_THREAD_LOCAL.get();
    }

    public static MyOutputDTO.MyInputDTO getMyInput(){
        return MY_INPUT_THREAD_LOCAL.get();
    }

    public static void setMyOutput(MyOutputDTO outputDTO){
        MY_OUTPUT_THREAD_LOCAL.set(outputDTO);
    }

    public static void setMyInput(MyOutputDTO.MyInputDTO input){
        MY_INPUT_THREAD_LOCAL.set(input);
    }
}
