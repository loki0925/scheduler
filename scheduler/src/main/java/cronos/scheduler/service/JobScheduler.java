package cronos.scheduler.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class JobScheduler {

    private final JobService jobService;

    public JobScheduler(JobService jobService) {
        this.jobService = jobService;
    }

    @Scheduled(fixedRate = 60000) // runs every 1 min
    public void runJobs() {
        jobService.getAllJobs().forEach(job -> {
            if ("ACTIVE".equals(job.getStatus())) {
                System.out.println("Executing job: " + job.getJobName());
            }
        });
    }
}

