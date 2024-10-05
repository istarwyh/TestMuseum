package istarwyh.schelule;

import lombok.Getter;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author xiaohui 用于管理任务的队列，支持任务的添加和去重。
 */
@Getter
public class TaskQueue {
  /** 获取任务 */
  private final ConcurrentLinkedQueue<Task<?>> tasks = new ConcurrentLinkedQueue<>();

  /** 是否在执行后移除任务 */
  private final boolean removeAfterExecution;

  public TaskQueue(boolean removeAfterExecution) {
    this.removeAfterExecution = removeAfterExecution;
  }

  /** 添加任务，去重 */
  public void addTask(Task<?> task) {
    if (!tasks.contains(task)) {
      tasks.offer(task);
    }
  }
}
