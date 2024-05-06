package istarwyh.page_module_loader;

import istarwyh.page_module_loader.component.AbstractElement;
import istarwyh.page_module_loader.component.Point;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import lombok.Data;

/**
 * @author xiaohui
 */
@Data
public class DataContext<ELEMENT extends AbstractElement<?>, QUERY> {

  private Map<String, ELEMENT> elementMap;

  private QUERY queryDTO;

  @Nullable
  public ELEMENT getElement(String key) {
    return Optional.ofNullable(elementMap)
            .map(it -> it.get(key))
            .orElse(null);
  }

  public void setElementMap(Map<String, Point> elementMap) {
    this.elementMap = (Map<String, ELEMENT>)elementMap;
  }
}
