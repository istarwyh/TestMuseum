package istarwyh.moduleloader.display;

import com.alibaba.fastjson2.JSON;
import com.google.common.base.CaseFormat;
import istarwyh.moduleloader.component.Module;
import istarwyh.moduleloader.component.*;
import istarwyh.moduleloader.constructor.*;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModuleLoader {

    @NotNull
    private final ViewStructure viewStructure;
    private final DataContext context;

    public final Map<String, PageModuleConstructor<?>> componentConstructorMap = new HashMap<>(8);


    private ModuleLoader(@NotNull ViewStructure viewStructure, DataContext context) {
        this.viewStructure = viewStructure;
        this.context = context;

        {
            componentConstructorMap.put(
                    toUpperUnderScoreName(Module.class.getSimpleName()),
                    ModuleConstructor.empty()
            );
            componentConstructorMap.put(
                    toUpperUnderScoreName(AnnularChart.class.getSimpleName()),
                    AnnularChartConstructor.empty()
            );
            componentConstructorMap.put(
                    toUpperUnderScoreName(GraphLevel.class.getSimpleName()),
                    GraphLevelConstructor.empty()
            );
            componentConstructorMap.put(
                    toUpperUnderScoreName(Block.class.getSimpleName()),
                    BlockConstructor.empty()
            );
            componentConstructorMap.put(
                    toUpperUnderScoreName(MapBusiness.class.getSimpleName()),
                    MapBusinessConstructor.empty()
            );
            componentConstructorMap.put(
                    toUpperUnderScoreName(MainPoint.class.getSimpleName()),
                    MainPointConstructor.empty()
            );
            componentConstructorMap.put(
                    toUpperUnderScoreName(Point.class.getSimpleName()),
                    PointConstructor.empty()
            );
        }
    }

    @NotNull
    private static String toUpperUnderScoreName(String moduleClass) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, moduleClass);
    }


    public static ModuleLoader createModuleLoader(ViewStructure viewStructure, DataContext context) {
        return new ModuleLoader(viewStructure, context);
    }

    @Data
    public static class DataContext{

        private Map<String, BaseElement> elementMap;

        private String bizCode;
    }

    public PageModule<?> parse() {
        return setBoardModuleData(parseBoardModule(viewStructure, context));
    }

    private PageModule<?> setBoardModuleData(PageModule<?> pageModule) {
        String childData = Optional.ofNullable(pageModule.getData())
                .map(JSON::toJSONString)
                // 确保是模块
                .filter(it -> it.contains("moduleTypeCode"))
                .orElse(null);
        if(childData == null){
            return pageModule;
        }
        pageModule.setData(getChild(childData, context));
        if(pageModule.getData() instanceof List){
            ((List<PageModule<?>>) pageModule.getData()).forEach(this::setBoardModuleData);
            return pageModule;
        }else {
            return setBoardModuleData(pageModule);
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
                    .map(it -> parseBoardModule(it, context))
                    .toList();

        }else {
            child = parseBoardModule(ViewStructure.of(childData), context);
        }
        return child;
    }

    private PageModule<?> parseBoardModule(ViewStructure viewStructure, DataContext context) {
        String moduleTypeCode = viewStructure.getModuleTypeCode();
        PageModuleConstructor<?> pageModuleConstructor = componentConstructorMap.get(moduleTypeCode);
        if(pageModuleConstructor == null){
            throw new IllegalArgumentException("should define a component constructor of " + moduleTypeCode);
        }
        return pageModuleConstructor.build(viewStructure, context);
    }

}
