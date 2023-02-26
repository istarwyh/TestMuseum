package istarwyh.moduleloader.constructor;

import istarwyh.moduleloader.component.Block;


public class BlockConstructor implements ComponentConstructor<Block> {

    public static BlockConstructor empty() {
        return new BlockConstructor();
    }
}
