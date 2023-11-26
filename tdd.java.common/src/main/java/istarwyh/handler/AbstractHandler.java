package istarwyh.handler;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Optional.ofNullable;

/**
 * @author xiaohui
 */
public abstract class AbstractHandler<OutPut> {

    @Getter @Setter
    protected AbstractHandler<OutPut> left;

    @Getter @Setter
    protected AbstractHandler<OutPut> right;

    /**
     * execute
     * @return left node or right node or a concrete OutPut instance
     */
    public abstract OutPut execute();

    @SafeVarargs
    @NotNull
    public final AbstractHandler<OutPut> constructHandlerTreeByPreOrder(AbstractHandler<OutPut>... abstractHandlers){
        final AbstractHandler<OutPut> root = abstractHandlers[0];
        return ofNullable(this.buildBiTreeByPreOrder(root,0, abstractHandlers))
                .orElseThrow(() -> new IllegalCallerException("root cannot be null!"));
    }

    @Nullable
    private AbstractHandler<OutPut> buildBiTreeByPreOrder(AbstractHandler<OutPut> root, int index, AbstractHandler<OutPut>[] abstractHandlers) {
        if (root == null){
            return null;
        }
        int leftIndex = 2 * index + 1;
        if(leftIndex >= abstractHandlers.length){
            return root;
        }
        root.setLeft(abstractHandlers[leftIndex]);

        int rightIndex = 2 * (index + 1);
        if(rightIndex >= abstractHandlers.length){
            return root;
        }
        root.setRight(abstractHandlers[rightIndex]);

        this.buildBiTreeByPreOrder(root.getLeft(),leftIndex, abstractHandlers);
        this.buildBiTreeByPreOrder(root.getRight(),rightIndex, abstractHandlers);
        return root;
    }
}
