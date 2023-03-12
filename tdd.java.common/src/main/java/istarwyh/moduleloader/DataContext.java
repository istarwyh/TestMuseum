package istarwyh.moduleloader;

import lombok.Data;

import java.util.Map;

@Data
public class DataContext<QueryDTO> {

    private Map<String, BaseElement> elementMap;

    private QueryDTO queryDTO;
}
