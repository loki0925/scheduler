package cronos.scheduler.executor;

import cronos.scheduler.entity.Job;
import cronos.scheduler.entity.enums.JobType;
import cronos.scheduler.scheduler.EmailJobExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobExecutorFactory {

    private final EmailJobExecutor emailJobExecutor;

    public void execute(Job job) throws Exception {
        if (job.getJobType() == JobType.EMAIL) {
            emailJobExecutor.execute(job);
        } else {
            throw new IllegalArgumentException("Unsupported job type");
        }
    }
}

