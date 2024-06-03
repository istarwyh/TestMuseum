package istarwyh.junit5.provider.model;

import static istarwyh.util.TypeUtils.isJsonType;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author xiaohui
 */
@RequiredArgsConstructor
@Getter
public class TestCase<IN, OUT> {

    /**
     * input args of function
     */
    private final IN input;

    /**
     * output result of function*
     */
    private final OUT output;

    /**
     *
     * @param inType one level type
     * @return {@link IN}
     */
    public IN getInput(Class<IN> inType) {
        if(isJsonType(input)){
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
        if(isJsonType(input)){
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
        if(isJsonType(output)){
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
        if(isJsonType(output)){
            return JSON.parseObject(JSON.toJSONString(output),typeReference) ;
        }else {
            return output;
        }
    }

}
