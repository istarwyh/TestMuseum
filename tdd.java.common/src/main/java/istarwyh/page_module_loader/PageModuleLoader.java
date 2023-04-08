package istarwyh.page_module_loader;

import com.alibaba.fastjson2.JSON;
import istarwyh.page_module_loader.bill.AbstractBillElement;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

@Slf4j
@RequiredArgsConstructor
public class PageModuleLoader {

    @NotNull
    private final PageModuleRawStructure pageModuleRawStructure;
    private final DataContext context;

    public static final Map<String, PageModuleConstructor<?, ?>> PAGE_MODULE_CONSTRUCTOR_MAP = new HashMap<>(8);

    static {
        // JDK SPI load from META-INF.services/
        ServiceLoader.load(PageModuleConstructor.class).forEach(PageModuleConstructor::register);
    }

    @SneakyThrows
    public static void registerPageModuleConstructor(@NotNull PageModuleConstructor<?, ?> pageModuleConstructor) {
        Class<? extends AbstractBillElement<?>> supportedElement = pageModuleConstructor.supportedElement();
        Method getModuleTypeCode = supportedElement.getMethod("getModuleTypeCode");
        String moduleTypeCode = (String)getModuleTypeCode.invoke(supportedElement.newInstance());
        PAGE_MODULE_CONSTRUCTOR_MAP.put( moduleTypeCode, pageModuleConstructor);
    }

    public static PageModuleLoader createModuleLoader(PageModuleRawStructure pageModuleRawStructure, DataContext context) {
        return new PageModuleLoader(pageModuleRawStructure, context);
    }

    public PageModule<?> parse() {
        PageModule<?> root = constructPageModule(pageModuleRawStructure, context);
        return fillData(root);
    }

    private PageModule<?> fillData(@NotNull PageModule<?> pageModule) {
        Object data = pageModule.getData();
        if (data instanceof List) {
            // child of same level should be the same element
            List<PageModule> children = ((List) data).stream()
                    .map(JSON::toJSONString)
                    .filter(it -> PageModuleRawStructure.isPageModuleStr((String)it))
                    .map(it -> PageModuleRawStructure.of((String)it))
                    .map(it -> constructPageModule((PageModuleRawStructure) it, context))
                    .toList();
            if(CollectionUtils.isEmpty(children)) {
                return pageModule;
            }
            pageModule.setData(children);
            children.forEach(this::fillData);
            return pageModule;
        }else if (data instanceof PageModule) {
            PageModuleRawStructure structure = PageModuleRawStructure.of(JSON.toJSONString(data));
            PageModule<?> child = constructPageModule(structure, context);
            pageModule.setData(child);
            return fillData(pageModule);
        }else {
            log.debug("pageModule:{}", JSON.toJSONString(pageModule));
            return pageModule;
        }
    }

    private PageModule<?> constructPageModule(PageModuleRawStructure pageModuleRawStructure, DataContext<Object> context) {
        String moduleTypeCode = pageModuleRawStructure.getModuleTypeCode();
        PageModuleConstructor<?, Object> pageModuleConstructor =
                (PageModuleConstructor<?, Object>) PAGE_MODULE_CONSTRUCTOR_MAP.get(moduleTypeCode);
        if (pageModuleConstructor == null) {
            throw new IllegalArgumentException("should define a component constructor of " + moduleTypeCode);
        }
        return pageModuleConstructor.construct(pageModuleRawStructure, context);
    }

}
