package istarwyh.moduleloader;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static istarwyh.moduleloader.util.NameConverter.toUpperUnderScoreName;

@Slf4j
@RequiredArgsConstructor
public class ModuleLoader {

    @NotNull
    private final ViewStructure viewStructure;
    private final DataContext context;

    public static final Map<String, PageModuleConstructor<?, ?>> PAGE_MODULE_CONSTRUCTOR_MAP = new HashMap<>(8);

    static {
        // JDK SPI load from META-INF.services/
        ServiceLoader.load(PageModuleConstructor.class).forEach(PageModuleConstructor::register);
    }

    public static void registerPageModuleConstructor(@NotNull PageModuleConstructor<?, ?> pageModuleConstructor) {
        PAGE_MODULE_CONSTRUCTOR_MAP.put(
                toUpperUnderScoreName(pageModuleConstructor.support().getSimpleName()),
                pageModuleConstructor);
    }

    public static ModuleLoader createModuleLoader(ViewStructure viewStructure, DataContext context) {
        return new ModuleLoader(viewStructure, context);
    }

    public PageModule<?> parse() {
        PageModule<?> root = constructPageModule(viewStructure, context);
        return fillData(root);
    }

    private PageModule<?> fillData(@NotNull PageModule<?> pageModule) {
        Object data = pageModule.getData();
        if (data instanceof List) {
            // child of same level should be the same element
            List<PageModule> children = ((List) data).stream()
                    .map(JSON::toJSONString)
                    .filter(it -> ViewStructure.isPageModuleStr((String)it))
                    .map(it -> ViewStructure.of((String)it))
                    .map(it -> constructPageModule((ViewStructure) it, context))
                    .toList();
            if(CollectionUtils.isEmpty(children)) {
                return pageModule;
            }
            pageModule.setData(children);
            children.forEach(this::fillData);
            return pageModule;
        }else if (data instanceof PageModule) {
            ViewStructure structure = ViewStructure.of(JSON.toJSONString(data));
            PageModule<?> child = constructPageModule(structure, context);
            pageModule.setData(child);
            return fillData(pageModule);
        }else {
            log.debug("pageModule:{}", JSON.toJSONString(pageModule));
            return pageModule;
        }
    }

    private PageModule<?> constructPageModule(ViewStructure viewStructure, DataContext<Object> context) {
        String moduleTypeCode = viewStructure.getModuleTypeCode();
        PageModuleConstructor<?, Object> pageModuleConstructor =
                (PageModuleConstructor<?, Object>) PAGE_MODULE_CONSTRUCTOR_MAP.get(moduleTypeCode);
        if (pageModuleConstructor == null) {
            throw new IllegalArgumentException("should define a component constructor of " + moduleTypeCode);
        }
        return pageModuleConstructor.construct(viewStructure, context);
    }

}
