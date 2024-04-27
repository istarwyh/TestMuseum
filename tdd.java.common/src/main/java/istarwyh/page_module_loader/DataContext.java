package istarwyh.page_module_loader;

import istarwyh.page_module_loader.bill.BillElementDTO;
import lombok.Data;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

@Data
public class DataContext<QueryDTO> {

  private Map<String, BillElementDTO> elementMap;

  private QueryDTO queryDTO;

  @Nullable
  public BillElementDTO getElement(String key) {
    return Optional.ofNullable(elementMap).map(it -> it.get(key)).orElse(null);
  }
}
