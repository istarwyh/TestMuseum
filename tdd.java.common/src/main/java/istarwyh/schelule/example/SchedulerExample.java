package istarwyh.schelule.example;

import istarwyh.schelule.TaskQueue;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author xiaohui 负责调度任务的类，包含一个后台线程来处理任务队列
 */
@Component
public class SchedulerExample implements InitializingBean {

  private Scheduler scheduler;
  private TaskQueue taskQueue;

  public void addJob(
      String jobName, String jobGroup, Class<? extends Job> jobClass, String cronExpression)
      throws SchedulerException {
    JobDataMap jobDataMap = new JobDataMap();
    jobDataMap.put("taskQueue", taskQueue);
    JobDetail jobDetail =
        JobBuilder.newJob(jobClass)
            .withIdentity(jobName, jobGroup)
            .usingJobData(jobDataMap)
            .build();

    Trigger trigger =
        TriggerBuilder.newTrigger()
            .withIdentity(jobName + "Trigger", jobGroup)
            .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
            .build();

    scheduler.scheduleJob(jobDetail, trigger);
  }

  public void shutdown() throws SchedulerException {
    scheduler.shutdown();
  }

  @EventListener
  public void handleTaskEvent(TaskExampleEvent event) {
    taskQueue.addTask(event.getTask());
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.taskQueue = new TaskQueue(false);
    this.scheduler = StdSchedulerFactory.getDefaultScheduler();
    this.addJob("taskJob", "group1", TaskExampleJob.class, "0/5 * * * * ?");
    this.scheduler.start();
  }
}
