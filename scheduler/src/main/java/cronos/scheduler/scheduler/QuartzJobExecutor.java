package cronos.scheduler.scheduler;

import cronos.scheduler.service.JobService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuartzJobExecutor implements org.quartz.Job {

    @Autowired
    private JobService jobService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long jobId = context.getMergedJobDataMap().getLong("jobId");

        jobService.executeJob(jobId);
    }
}
