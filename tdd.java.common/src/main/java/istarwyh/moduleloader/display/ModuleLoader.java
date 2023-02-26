package istarwyh.moduleloader.display;

import com.alibaba.fastjson2.JSON;
import com.google.common.base.CaseFormat;
import istarwyh.moduleloader.component.*;
import istarwyh.moduleloader.component.Module;
import istarwyh.moduleloader.constructor.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModuleLoader {

    @NotNull
    private final ViewStructure viewStructure;
    private final Object context;

    public final Map<String, ComponentConstructor<?>> componentConstructorMap = new HashMap<>(8);


    private ModuleLoader(@NotNull ViewStructure viewStructure, Object context) {
        this.viewStructure = viewStructure;
        this.context = context;

        {
            componentConstructorMap.put(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, Module.class.getSimpleName()),
                    ModuleConstructor.empty()
            );
            componentConstructorMap.put(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, Block.class.getSimpleName()),
                    BlockConstructor.empty()
            );
            componentConstructorMap.put(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, MapBusiness.class.getSimpleName()),
                    MapBusinessConstructor.empty()
            );
            componentConstructorMap.put(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, MainPoint.class.getSimpleName()),
                    MainPointConstructor.empty()
            );
        }
    }

    public static ModuleLoader createModuleLoader(ViewStructure viewStructure, Object context) {
        return new ModuleLoader(viewStructure, context);
    }

    public BoardModule<?> parse() {
        BoardModule<?> boardModule = parseBoardModule(viewStructure, context);
        return setBoardModuleData(boardModule);
    }

    private BoardModule<?> setBoardModuleData(BoardModule<?> boardModule) {
        String childData = Optional.ofNullable(boardModule.getData())
                // todo  这里如果使用JSON::toJSONString会导致subjectCode直接丢失，不能理解为什么？？
                .map(Object::toString)
                // 确保是模块
                .filter(it -> it.contains("moduleTypeCode"))
                .orElse(null);
        if(childData == null){
            return boardModule;
        }
        boardModule.setData(getChild(childData, context));
        if(boardModule.getData() instanceof List){
            ((List<BoardModule<?>>)boardModule.getData()).forEach(this::setBoardModuleData);
            return boardModule;
        }else {
            return setBoardModuleData(boardModule);
        }
    }


    private Object getChild(String childData, Object context) {
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

    private BoardModule<?> parseBoardModule(ViewStructure viewStructure, Object queryDTO) {
        ComponentConstructor<?> componentConstructor = componentConstructorMap.get(viewStructure.getModuleTypeCode());
        if(componentConstructor == null){
            throw new IllegalArgumentException("should define a component constructor");
        }
        return componentConstructor.build(viewStructure, queryDTO);
    }

}
