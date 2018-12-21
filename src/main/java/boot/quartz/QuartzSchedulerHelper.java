package boot.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.CronScheduleBuilder.cronSchedule;

/**
 * Created by nicaisheng on 17/3/23.
 */
@Slf4j
//@Component
public class QuartzSchedulerHelper {

    @Autowired
    private Scheduler scheduler;

    private static final String DEFAULT_GROUP = "defaultGroup";

    public void schedule(TaskDefinition task) {
        try {
            Class<?> clazz = Class.forName(task.getTaskClass());
            if (!Job.class.isAssignableFrom(clazz))
                return;

            JobDetail job = newJob((Class<? extends Job>) clazz)
                    .withIdentity(task.getTaskName(), DEFAULT_GROUP)
                    .build();

            Trigger trigger = newTrigger()
                    .withIdentity(task.getTaskName(), DEFAULT_GROUP)
                    .startNow()
                    .withSchedule(cronSchedule(task.getCron()))
                    .build();

            if (task.isRemove()) {
                // 删掉被禁用的任务列表
                log.debug("remove the scheduled task: {}", task.getTaskName());
                scheduler.deleteJob(job.getKey());
            } else {
                try {
                    scheduler.scheduleJob(job, trigger);
                } catch (ObjectAlreadyExistsException e) {
                    log.debug("Seems the job is already exist, replace it!", e);

                    log.info("reschedule task '{}' with trigger '{}'", task.getTaskName(), task.getCron());
                    scheduler.rescheduleJob(trigger.getKey(), trigger);
                }
            }
        } catch (ClassNotFoundException e) {
            log.error("task class doesn't exist!", e);
        } catch (SchedulerException e) {
            log.error("failed to schedule task", e);
        }
    }
}
