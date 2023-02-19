package istarwyh.moduleloader.constructor;

import com.alibaba.fastjson2.JSON;
import istarwyh.moduleloader.component.Block;

public class BlockConstructor implements BoardConstructor<Block> {

    private BlockConstructor(ViewStructure viewStructure) {
    }

    public static BlockConstructor createBlockConstructor(ViewStructure viewStructure) {
        return new BlockConstructor(viewStructure);
    }

    @Override
    public Block build(ViewStructure viewStructure, Object queryDTO) {
        return JSON.parseObject(viewStructure.getStructureStr(), Block.class);
    }
}
