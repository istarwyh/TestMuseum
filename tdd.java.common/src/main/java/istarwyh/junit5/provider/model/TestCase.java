package istarwyh.junit5.provider.model;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;

public class TestCase<IN, OUT> {

    /**
     * input args of function
     */
    private IN input;

    /**
     * output result of function
     */
    private OUT output;

    public TestCase() {}

    public TestCase(IN input, OUT output) {
        this.input = input;
        this.output = output;
    }

    /**
     *
     * @param inType one level type
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
     * @param typeReference any type
     * @return {@link IN}
     */
    public IN getInput(TypeReference<IN> typeReference) {
        if(isJSON(input)){
            return JSON.parseObject(JSON.toJSONString(input),typeReference) ;
        }else {
            return input ;
        }
    }

    /**
     *
     * @param outType one level type
     * @return {@link OUT}
     */
    public OUT getOutput(Class<OUT> outType) {
        if(isJSON(output)){
            return JSON.parseObject(JSON.toJSONString(output),outType) ;
        }else {
            return output ;
        }
    }

    /**
     *
     * @param typeReference any type
     * @return {@link OUT}
     */
    public OUT getOutput(TypeReference<OUT> typeReference) {
        if(isJSON(output)){
            return JSON.parseObject(JSON.toJSONString(output),typeReference) ;
        }else {
            return output;
        }
    }

    public <T> boolean isJSON(T o){
        return o instanceof JSONObject || o instanceof JSONArray;
    }

    /**
     *
     * @return {@link IN} primitive type
     */
    public IN getInput() {
        return input;
    }

    /**
     *
     * @return {@link OUT} primitive type
     */
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
