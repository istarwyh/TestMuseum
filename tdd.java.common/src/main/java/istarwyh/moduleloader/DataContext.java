package istarwyh.moduleloader;

import lombok.Data;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

@Data
public class DataContext<QueryDTO> {

    private Map<String, ElementDTO> elementMap;

    private QueryDTO queryDTO;

    @Nullable
    public ElementDTO getElement(String key) {
        return Optional.ofNullable(elementMap).map(it -> it.get(key)).orElse(null);
    }
}
