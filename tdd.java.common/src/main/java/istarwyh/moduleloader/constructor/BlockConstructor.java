package istarwyh.moduleloader.constructor;

import istarwyh.moduleloader.component.Block;

public class BlockConstructor implements ComponentConstructor<Block> {

    private BlockConstructor(ViewStructure viewStructure) {
    }

    public static BlockConstructor createBlockConstructor(ViewStructure viewStructure) {
        return new BlockConstructor(viewStructure);
    }
}
