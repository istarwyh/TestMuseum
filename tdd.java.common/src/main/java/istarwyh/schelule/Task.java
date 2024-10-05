package istarwyh.schelule;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author mac
 * 表示一个可执行的任务，使用方法引用来执行。
 */
public class Task<T> {
    private final Consumer<T> taskMethod;
    private final T param;

    public Task(Consumer<T> taskMethod, T param) {
        this.taskMethod = taskMethod;
        this.param = param;
    }

    public void execute() {
        taskMethod.accept(param);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task<?> task)) {
            return false;
        }
        return Objects.equals(taskMethod, task.taskMethod) &&
                Objects.equals(param, task.param);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskMethod, param);
    }
}