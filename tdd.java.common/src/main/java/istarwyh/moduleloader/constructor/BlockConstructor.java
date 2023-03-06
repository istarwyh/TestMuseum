package istarwyh.moduleloader.constructor;

import istarwyh.moduleloader.component.Block;


public class BlockConstructor implements PageModuleConstructor<Block> {

    public static BlockConstructor empty() {
        return new BlockConstructor();
    }
}
