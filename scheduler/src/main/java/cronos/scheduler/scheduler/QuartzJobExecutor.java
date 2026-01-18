package cronos.scheduler.scheduler;

import cronos.scheduler.entity.ExecutionLog;
import cronos.scheduler.entity.Job;
import cronos.scheduler.entity.JobEvent;
import cronos.scheduler.entity.enums.JobStatus;
import cronos.scheduler.entity.enums.LogLevel;
import cronos.scheduler.executor.JobExecutorFactory;
import cronos.scheduler.service.JobEventProducer;
import cronos.scheduler.entity.JobEvent;
import cronos.scheduler.repo.ExecutionLogRepository;
import cronos.scheduler.repo.JobRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class QuartzJobExecutor implements org.quartz.Job {

    private final JobRepo jobRepository;
    private final ExecutionLogRepository executionLogRepository;
    private final JobEventProducer jobEventProducer;
    private final JobExecutorFactory jobExecutorFactory;

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {

        Long jobId = context.getMergedJobDataMap().getLong("jobId");

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

        ExecutionLog log = new ExecutionLog();
        log.setJob(job);
        log.setStartedAt(LocalDateTime.now());
        log.setStatus(JobStatus.RUNNING);
        log.setLogLevel(LogLevel.INFO);
        executionLogRepository.save(log);

        // 📣 Publish JOB_STARTED event
        //jobEventProducer.publish(buildEvent(job, "JOB_STARTED", null));

        try {
            // 🔥 ACTUAL JOB LOGIC
            jobExecutorFactory.execute(job);
            System.out.println("Executing Job ID: " + jobId);

            job.setStatus(JobStatus.COMPLETED);
            log.setStatus(JobStatus.COMPLETED);

            // 📣 Publish JOB_COMPLETED event
            //jobEventProducer.publish(buildEvent(job, "JOB_COMPLETED", null));

        } catch (Exception e) {

            job.setStatus(JobStatus.FAILED);
            log.setStatus(JobStatus.FAILED);
            log.setErrorMessage(e.getMessage());

            // 📣 Publish JOB_FAILED event
          //  jobEventProducer.publish(buildEvent(job, "JOB_FAILED", e.getMessage()));

            throw new JobExecutionException(e, true); // 🔁 Quartz retry

        } finally {

            log.setEndedAt(LocalDateTime.now());
            jobRepository.save(job);
            executionLogRepository.save(log);
        }
    }

    private JobEvent buildEvent(Job job, String eventType, String message) {
        return JobEvent.builder()
                .jobId(job.getId())
                .jobName(job.getJobName())
                .eventType(eventType)
                .status(job.getStatus().name())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
