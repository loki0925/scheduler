package cronos.scheduler.service;

import cronos.scheduler.entity.Job;
import cronos.scheduler.scheduler.QuartzJobExecutor;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;

@Service
public class JobSchedulerService {

    @Autowired
    private Scheduler scheduler;

    public void scheduleOneTimeJob(Job job) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(QuartzJobExecutor.class)
                .withIdentity("job_" + job.getId())
                .usingJobData("jobId", job.getId())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .startAt(Date.from(job.getScheduledAt()
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void scheduleRecurringJob(Job job, String cron) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(QuartzJobExecutor.class)
                .withIdentity("job_" + job.getId())
                .usingJobData("jobId", job.getId())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}

