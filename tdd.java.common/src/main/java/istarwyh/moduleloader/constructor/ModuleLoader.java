package istarwyh.moduleloader.constructor;

import com.alibaba.fastjson2.JSON;
import com.google.common.base.CaseFormat;
import istarwyh.moduleloader.component.*;
import istarwyh.moduleloader.component.Module;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ModuleLoader {

    @NotNull
    private final ViewStructure viewStructure;
    private final Object queryDTO;

    public final Map<String, BoardConstructor<?>> moduleTypeMap = new HashMap<>(8);


    private ModuleLoader(@NotNull ViewStructure viewStructure, Object queryDTO) {
        this.viewStructure = viewStructure;
        this.queryDTO = queryDTO;

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
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, MapBusinessConstructor.class.getSimpleName()),
                    MapBusinessConstructor.createComponentConverterConstructor(viewStructure,queryDTO)
            );
        }
    }

    public static ModuleLoader createModuleLoader(ViewStructure viewStructure, Object queryDTO) {
        return new ModuleLoader(viewStructure, queryDTO);
    }

    public BoardModule<?> parse() {
        ViewStructure structure;
        String nodeData;
        String moduleTypeCode;
        nodeData = viewStructure.getStructureStr();
        if(nodeData.startsWith("[")){
            throw new IllegalArgumentException("board or module data must be a object instead of array");
        }
        Node node = JSON.parseObject(nodeData, Node.class);
        moduleTypeCode = node.getModuleTypeCode();
        structure = ViewStructure.builder().structureStr(nodeData).build();
        BoardModule<?> boardModule = moduleTypeMap.get(moduleTypeCode).build(structure,queryDTO);
        BoardModule<?> headModule = boardModule;

        while(true){
            nodeData = Optional.ofNullable(boardModule.getData())
                    .map(Object::toString)
                    // 确保是模块
                    .filter(it -> it.contains("moduleTypeCode"))
                    .orElse(null);
            if(nodeData == null){
                return headModule;
            }
            // todo 处理子节点为数组的情况
            if(nodeData.startsWith("[")){

            }
            node = JSON.parseObject(nodeData, Node.class);
            moduleTypeCode = node.getModuleTypeCode();
            nodeData = node.getData();

            structure = ViewStructure.builder().structureStr(nodeData).build();
            BoardModule<?> childBoardModule = moduleTypeMap.get(moduleTypeCode).build(structure, queryDTO);
            boardModule.setData(childBoardModule);
            boardModule = (BoardModule<?>) boardModule.getData();
        }
    }

}
