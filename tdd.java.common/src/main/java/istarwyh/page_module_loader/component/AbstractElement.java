package istarwyh.page_module_loader.component;

import static java.util.Optional.ofNullable;

import com.google.common.base.CaseFormat;
import istarwyh.page_module_loader.ElementDTO;
import istarwyh.page_module_loader.PageModule;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

/**
 * @author xiaohui
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractElement<DATA> extends ElementDTO implements PageModule<DATA> {

  private DATA data;

  @Override
  public String getModuleTypeCode() {
    return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, this.getClass().getSimpleName());
  }

  @Override
  @Nullable
  public DATA getData() {
    return data;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void setData(Object data) {
    this.data = (DATA) data;
  }

  @SuppressWarnings("unchecked")
  public <ELEMENT extends AbstractElement<?>> void fillWith(ELEMENT materiaElement) {
    if (materiaElement == null) {
      return;
    }
    this.setAmount(ofNullable(this.getAmount()).orElse(materiaElement.getAmount()));
    this.setNumber(ofNullable(this.getNumber()).orElse(materiaElement.getNumber()));
    this.setTime(ofNullable(this.getTime()).orElse(materiaElement.getTime()));
    List<ElementDTO> details = materiaElement.getDetails();
    this.setData(ofNullable(this.getData()).orElse((DATA) details));
  }

  public boolean dataEmpty(){
    if(data == null){
      return true;
    }
    return !(data instanceof List) || ((List<?>) data).isEmpty();
  }
}
