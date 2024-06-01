package istarwyh.page_module_loader;

import com.alibaba.fastjson2.JSON;
import istarwyh.page_module_loader.component.AbstractElement;
import istarwyh.util.ReflectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static istarwyh.util.ReflectionUtils.getAllClassesImplementingInterface;
import static istarwyh.util.ReflectionUtils.getInstanceWithoutArgs;

/**
 * @author xiaohui
 */
@Slf4j
@RequiredArgsConstructor
public class ModuleLoader {

    @NotNull
    private final ViewStructure viewStructure;

    @SuppressWarnings("rawtypes")
    private final DataContext context;

    public static final Map<String, ModuleConstructor<?, ?>> PAGE_MODULE_CONSTRUCTOR_MAP =
            new HashMap<>(8);

    //   在不借助Spring等框架的情况下，目前看来SPI和反射拿全部接口实现类/注解类就是将对象放到一个集合里唯二的办法
    static {

        // JDK SPI load from META-INF.services/
//        ServiceLoader.load(PageModuleConstructor.class).forEach(PageModuleConstructor::register);
        getAllClassesImplementingInterface(ModuleConstructor.class).stream()
                .map(ReflectionUtils::getInstanceWithoutArgs)
                .forEach(ModuleConstructor::register);
    }

    @SneakyThrows({NoSuchMethodException.class, IllegalAccessException.class, InvocationTargetException.class})
    public static void registerPageModuleConstructor(@NotNull ModuleConstructor<?, ?> moduleConstructor) {
        Class<? extends AbstractElement<?>> supportedElement = moduleConstructor.supportedElement();
        Method getModuleTypeCode = supportedElement.getMethod("getModuleTypeCode");
        String moduleTypeCode = (String) getModuleTypeCode.invoke(getInstanceWithoutArgs(supportedElement));
        PAGE_MODULE_CONSTRUCTOR_MAP.put(moduleTypeCode, moduleConstructor);
    }

    public static <ELEMENT extends AbstractElement<?>, QUERY> ModuleLoader
    createModuleLoader(ViewStructure viewStructure, DataContext<ELEMENT,QUERY> context) {
        return new ModuleLoader(viewStructure, context);
    }

    @SuppressWarnings("unchecked")
    public PageModule<?> parse() {
        PageModule<?> root = constructPageModule(viewStructure, context);
        return fillData(root);
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    private PageModule<?> fillData(@NotNull PageModule<?> pageModule) {
        Object data = pageModule.getData();
        if (data instanceof List) {
            // child of same level should be the same element
            List<PageModule> children = ((List) data).stream()
                    .map(JSON::toJSONString)
                    .filter(it -> ViewStructure.isPageModuleStr((String) it))
                    .map(it -> ViewStructure.of((String) it))
                    .map(it -> constructPageModule((ViewStructure) it, context))
                    .toList();
            if (children.isEmpty()) {
                return pageModule;
            }
            pageModule.setData(children);
            children.forEach(this::fillData);
            return pageModule;
        } else if (data instanceof PageModule) {
            ViewStructure structure = ViewStructure.of(JSON.toJSONString(data));
            PageModule<?> child = constructPageModule(structure, context);
            pageModule.setData(child);
            return fillData(pageModule);
        } else {
            log.debug("pageModule:{}", JSON.toJSONString(pageModule));
            return pageModule;
        }
    }

    @SuppressWarnings("unchecked")
    private <ELEMENT extends AbstractElement<?>, QUERY> PageModule<?>
    constructPageModule(ViewStructure viewStructure, DataContext<ELEMENT,QUERY> context) {
        String moduleTypeCode = viewStructure.getModuleTypeCode();
        ModuleConstructor<ELEMENT, QUERY> moduleConstructor =
                (ModuleConstructor<ELEMENT, QUERY>) PAGE_MODULE_CONSTRUCTOR_MAP.get(moduleTypeCode);
        if (moduleConstructor == null) {
            throw new IllegalArgumentException("should define a component constructor of " + moduleTypeCode);
        }
        return moduleConstructor.construct(viewStructure, context);
    }

}
