package istarwyh.handler;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Optional.ofNullable;

public abstract class Handler<T> {

    @Getter @Setter
    protected Handler<T> left;

    @Getter @Setter
    protected Handler<T> right;

    /**
     *
     * @return left node or right node or a concrete T instance
     */
    public abstract T execute();

    @SafeVarargs
    @NotNull
    public final Handler<T> constructHandlerTreeByPreOrder(Handler<T>... handlers){
        final Handler<T> root = handlers[0];
        return ofNullable(this.buildBiTreeByPreOrder(root,0,handlers))
                .orElseThrow(() -> new IllegalCallerException("root cannot be null!"));
    }

    @Nullable
    private Handler<T> buildBiTreeByPreOrder(Handler<T> root, int index, Handler<T>[] handlers) {
        if (root == null){
            return null;
        }
        int leftIndex = 2 * index + 1;
        if(leftIndex >= handlers.length){
            return root;
        }
        root.setLeft(handlers[leftIndex]);

        int rightIndex = 2 * (index + 1);
        if(rightIndex >= handlers.length){
            return root;
        }
        root.setRight(handlers[rightIndex]);

        this.buildBiTreeByPreOrder(root.getLeft(),leftIndex,handlers);
        this.buildBiTreeByPreOrder(root.getRight(),rightIndex,handlers);
        return root;
    }
}
