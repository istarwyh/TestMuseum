package istarwyh.page_module_loader;

import static com.alibaba.fastjson2.JSON.parseObject;

import istarwyh.page_module_loader.component.AbstractElement;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author xiaohui
 */
public interface ModuleConstructor<ELEMENT extends AbstractElement<?>, QUERY> {

  /**
   * @return supported page module
   */
  @NotNull
  default Class<ELEMENT> supportedElement() {
    Type[] genericInterfaces = this.getClass().getGenericInterfaces();
    for (Type type : genericInterfaces) {
      if (type instanceof ParameterizedType
          && ((ParameterizedType) type)
              .getRawType()
              .getTypeName()
              .equals(ModuleConstructor.class.getName())) {
        Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
        if (actualTypeArgument != null) {
          return getClassFromParameterizedType(actualTypeArgument);
        }
      }
    }
    throw new IllegalArgumentException("Invalid interface");
  }

  @Nullable
  private static <T> Class<T> getClassFromParameterizedType(Type actualTypeArgument) {
    if (actualTypeArgument instanceof Class) {
      return (Class<T>) actualTypeArgument;
    } else {
      return getClassFromParameterizedType(((ParameterizedType) actualTypeArgument).getRawType());
    }
  }

  /**
   * eg. build with {@link ElementDTO}
   *
   * @param viewStructure {@link ViewStructure}
   * @param context {@link DataContext}
   * @return {@link PageModule}
   */
  default ELEMENT construct(
          ViewStructure viewStructure, DataContext<ELEMENT, QUERY> context) {
    ELEMENT emptyElement = parseObject(viewStructure.getStructureStr(), this.supportedElement());
    ELEMENT materiaElement = context.getElement(emptyElement.getSubjectCode());
    emptyElement.fillWith(materiaElement);
    return emptyElement;
  }

  /** register the page module constructor in a map by SPI */
  default void register() {
    ModuleLoader.registerPageModuleConstructor(this);
  }
}
