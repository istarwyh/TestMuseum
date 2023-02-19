package istarwyh.moduleloader.constructor;

import com.alibaba.fastjson2.JSON;
import com.google.common.base.CaseFormat;
import istarwyh.moduleloader.component.*;
import istarwyh.moduleloader.component.Module;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModuleLoader {

    @NotNull
    private final ViewStructure viewStructure;
    private final Object context;

    public final Map<String, BoardConstructor<?>> moduleTypeMap = new HashMap<>(8);


    private ModuleLoader(@NotNull ViewStructure viewStructure, Object context) {
        this.viewStructure = viewStructure;
        this.context = context;

        {
            moduleTypeMap.put(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, Module.class.getSimpleName()),
                    ModuleConstructor.createModuleConstructor(viewStructure)
            );
            moduleTypeMap.put(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, Block.class.getSimpleName()),
                    BlockConstructor.createBlockConstructor(viewStructure)
            );
            moduleTypeMap.put(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, MapBusiness.class.getSimpleName()),
                    MapBusinessConstructor.createComponentConverterConstructor(viewStructure, context)
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
        // 存储子元素的data
        String childData = Optional.ofNullable(boardModule.getData())
                .map(JSON::toJSONString)
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


    private Object getChild(String nodeData, Object queryDTO1) {
        Object child;
        if(nodeData.startsWith("[")){
            child = JSON.<String>parseArray(nodeData, String.class)
                    .stream()
                    .map(ViewStructure::of)
                    .toList()
                    .stream()
                    .map(it -> parseBoardModule(it, queryDTO1))
                    .toList();

        }else {
            child = parseBoardModule(ViewStructure.of(nodeData), queryDTO1);
        }
        return child;
    }

    private BoardModule<?> parseBoardModule(ViewStructure viewStructure, Object queryDTO) {
        return moduleTypeMap.get(viewStructure.getModuleTypeCode()).build(viewStructure, queryDTO);
    }

}
