package istarwyh.moduleloader;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static istarwyh.moduleloader.util.NameConverter.toUpperUnderScoreName;

@RequiredArgsConstructor
public class ModuleLoader {

    @NotNull
    private final ViewStructure viewStructure;
    private final DataContext context;

    public static final Map<String, PageModuleConstructor<?,?>> PAGE_MODULE_CONSTRUCTOR_MAP = new HashMap<>(8);

    static {
        // JDK SPI load from META-INF.services/
        ServiceLoader.load(PageModuleConstructor.class).forEach(PageModuleConstructor::register);
    }

    public static void registerPageModuleConstructor(@NotNull PageModuleConstructor<?,?> pageModuleConstructor) {
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
        String childDataStr = Optional.ofNullable(pageModule.getData())
                .map(JSON::toJSONString)
                // 确保是模块
                .filter(it -> it.contains("moduleTypeCode"))
                .orElse(null);
        if(childDataStr == null){
            return pageModule;
        }
        Object childData = getChild(childDataStr, context);
        pageModule.setData(childData);
        if(childData instanceof List){
            ((List<PageModule<?>>) pageModule.getData()).forEach(this::fillData);
            return pageModule;
        }else {
            return fillData(pageModule);
        }
    }

    private Object getChild(String childData, DataContext context) {
        Object child;
        if(childData.startsWith("[")){
            child = JSON.<String>parseArray(childData, String.class)
                    .stream()
                    .map(ViewStructure::of)
                    .toList()
                    .stream()
                    .map(it -> constructPageModule(it, context))
                    .toList();

        }else {
            child = constructPageModule(ViewStructure.of(childData), context);
        }
        return child;
    }

    private PageModule<?> constructPageModule(ViewStructure viewStructure, DataContext<Object> context) {
        String moduleTypeCode = viewStructure.getModuleTypeCode();
        PageModuleConstructor<?,Object> pageModuleConstructor =
                (PageModuleConstructor<?, Object>) PAGE_MODULE_CONSTRUCTOR_MAP.get(moduleTypeCode);
        if(pageModuleConstructor == null){
            throw new IllegalArgumentException("should define a component constructor of " + moduleTypeCode);
        }
        return pageModuleConstructor.construct(viewStructure, context);
    }

}
