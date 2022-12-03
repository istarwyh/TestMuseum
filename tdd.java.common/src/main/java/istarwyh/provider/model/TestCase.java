package istarwyh.provider.model;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

public class TestCase<IN, OUT> {

    /**
     * input args of function
     */
    private IN input;

    /**
     * output result of function
     */
    private OUT output;


    /**
     *
     * @param inType not primitive type like String,Integer,List
     * @return {@link IN}
     */
    public IN getInput(Class<IN> inType) {
        if(isJSON(input)){
            return JSON.parseObject(JSON.toJSONString(input),inType) ;
        }else {
            return input ;
        }
    }

    /**
     *
     * @param outType not primitive type like String,Integer,List
     * @return {@link OUT}
     */
    public OUT getOutput(Class<OUT> outType) {
        if(isJSON(output)){
            return JSON.parseObject(JSON.toJSONString(output),outType) ;
        }else {
            return output ;
        }
    }

    public <T> boolean isJSON(T o){
        return o instanceof JSONObject || o instanceof JSONArray;
    }

    public IN getInput() {
        return input;
    }

    public OUT getOutput() {
        return output;
    }

    public void setInput(IN input) {
        this.input = input;
    }

    public void setOutput(OUT output) {
        this.output = output;
    }
}
