package istarwyh.handler;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author xiaohui
 */
@Data
@Accessors(chain = true)
public class MyOutputDTO{

    private List<MyOutputDTO> outputs;

    public record MyInputDTO() {}
}
