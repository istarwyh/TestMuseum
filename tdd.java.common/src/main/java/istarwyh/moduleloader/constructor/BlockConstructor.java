package istarwyh.moduleloader.constructor;

import istarwyh.moduleloader.PageModuleConstructor;
import istarwyh.moduleloader.component.Block;


public class BlockConstructor implements PageModuleConstructor<Block,Void> {

    public static BlockConstructor empty() {
        return new BlockConstructor();
    }
}
