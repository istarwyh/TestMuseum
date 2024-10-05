package istarwyh.schelule.example;

import istarwyh.schelule.Task;
import istarwyh.schelule.TaskQueue;
import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * TaskJob 是一个 Quartz 作业，用于从任务队列中获取任务并执行。 它使用自定义线程池来管理任务的并发执行。
 *
 * <p>此作业每 5 分钟启动一次，最多可以处理 50 个任务。每个任务的执行时间在 1 到 3 秒之间， 并且任务可能会失败。
 *
 * @author mac
 */
public class TaskExampleJob implements Job {
  private static final int CORE_POOL_SIZE = 5;

  /** 最大线程数 */
  private static final int MAX_POOL_SIZE = 10;

  /** 空闲线程存活时间（秒） */
  private static final long KEEP_ALIVE_TIME = 60L;

  /**
   * 自定义线程池，用于执行任务。
   *
   * <p>选择参数的理由如下：
   *
   * <ul>
   *   <li><b>CORE_POOL_SIZE</b>: 设置为 5，适合每 5 分钟处理最多 50 个任务的场景，能够在任务量较少时保持高效。
   *   <li><b>MAX_POOL_SIZE</b>: 设置为 10，以应对高峰负载，确保在任务执行时间为 1 到 3 秒的情况下，能够及时处理所有任务。
   *   <li><b>KEEP_ALIVE_TIME</b>: 设置为 60 秒，确保空闲线程在一段时间后被回收，节省系统资源。
   *   <li><b>阻塞队列</b>: 使用有界的阻塞队列，如果队列满了，新的任务提交将会被阻塞或拒绝，避免无休止的任务堆积
   * </ul>
   */
  private static final ExecutorService executorService =
      new ThreadPoolExecutor(
          CORE_POOL_SIZE,
          MAX_POOL_SIZE,
          KEEP_ALIVE_TIME,
          TimeUnit.SECONDS,
          new ArrayBlockingQueue<>(100));

  /**
   * 执行作业的方法，从任务队列中获取任务并提交到线程池。
   *
   * @param context Quartz 的作业执行上下文，包含作业的相关信息。
   */
  @Override
  public void execute(JobExecutionContext context) {
    TaskQueue taskQueue = (TaskQueue) context.getJobDetail().getJobDataMap().get("taskQueue");
    System.out.println("Task executed at: " + LocalDateTime.now());

    while (!taskQueue.getTasks().isEmpty()) {
      Task<?> task = taskQueue.getTasks().poll();
      if (task != null) {
        executorService.submit(getTask(task, taskQueue));
      }
    }
  }

  @NotNull
  private static Runnable getTask(Task<?> task, TaskQueue taskQueue) {
    return () -> {
      try {
        // 模拟任务执行时间
        int executionTime = (int) (Math.random() * 3) + 1; // 1s ~ 3s
        Thread.sleep(executionTime * 1000L);
        task.execute();
        System.out.println("Task completed successfully: " + task);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.err.println("Task was interrupted: " + task);
      } catch (Exception e) {
        System.err.println("Task failed: " + task + " due to " + e.getMessage());
      } finally {
        // 可选择性地将任务重新添加到队列中
        if (!taskQueue.isRemoveAfterExecution()) {
          taskQueue.addTask(task);
        }
      }
    };
  }

  /** 关闭线程池，确保所有任务完成后再关闭。 */
  public static void shutdown() {
    executorService.shutdown();
    try {
      if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
        executorService.shutdownNow();
      }
    } catch (InterruptedException e) {
      executorService.shutdownNow();
    }
  }
}
