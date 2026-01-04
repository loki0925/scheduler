package cronos.scheduler.scheduler;

import cronos.scheduler.entity.ExecutionLog;
import cronos.scheduler.entity.Job;
import cronos.scheduler.entity.enums.JobStatus;
import cronos.scheduler.repo.ExecutionLogRepository;
import cronos.scheduler.repo.JobRepo;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class QuartzJobExecutor implements org.quartz.Job {

    private final JobRepo jobRepository;
    private final ExecutionLogRepository executionLogRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        Long jobId = context.getMergedJobDataMap().getLong("jobId");

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        ExecutionLog log = new ExecutionLog();
        log.setJob(job);
        log.setStartedAt(LocalDateTime.now());
        log.setStatus(JobStatus.RUNNING);
        executionLogRepository.save(log);

        try {
            // 🔥 ACTUAL JOB LOGIC
            System.out.println("Executing Job ID: " + jobId);

            job.setStatus(JobStatus.COMPLETED);
            log.setStatus(JobStatus.COMPLETED);

        } catch (Exception e) {

            job.setStatus(JobStatus.FAILED);
            log.setStatus(JobStatus.FAILED);
            log.setErrorMessage(e.getMessage());

            throw new JobExecutionException(e, true); // 🔁 retry
        } finally {

            log.setEndedAt(LocalDateTime.now());
            jobRepository.save(job);
            executionLogRepository.save(log);
        }
    }
}
