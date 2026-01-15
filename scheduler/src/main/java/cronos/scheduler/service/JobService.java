package cronos.scheduler.service;


import cronos.scheduler.entity.ExecutionLog;
import cronos.scheduler.entity.Job;
import cronos.scheduler.entity.enums.JobStatus;
import cronos.scheduler.entity.enums.JobType;
import cronos.scheduler.repo.ExecutionLogRepository;
import cronos.scheduler.repo.JobRepo;
import jakarta.transaction.Transactional;
import org.hibernate.type.internal.ParameterizedTypeImpl;
import org.quartz.SchedulerException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobService {

    private final JobRepo jobRepository;

    private final ExecutionLogRepository executionLogRepository;

    private final JobSchedulerService jobSchedulerService;

    public JobService(JobRepo jobRepository, ExecutionLogRepository executionLogRepository, JobSchedulerService jobSchedulerService) {
        this.jobRepository = jobRepository;
        this.executionLogRepository = executionLogRepository;
        this.jobSchedulerService = jobSchedulerService;
    }

    public Job createJob(Job job) throws SchedulerException {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        job.setCreatedBy(username);
        Job savedJob = jobRepository.save(job);

        if (savedJob.getRecurrenceRule() != null) {
            // Recurring job (EMAIL at 8 PM)
            jobSchedulerService.scheduleRecurringJob(
                    savedJob,
                    savedJob.getRecurrenceRule()
            );
        } else {
            // One-time job
            jobSchedulerService.scheduleOneTimeJob(savedJob);
        }
        return jobRepository.save(job);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
    public List<Job> getAllJobsByUser(String username) {
        return jobRepository.findByCreatedBy(username);
    }

    // =========================
    // GET BY ID (ownership check)
    // =========================

    public Job getJobByIdAndUser(Long id, String username) {
        return jobRepository.findByIdAndCreatedBy(id, username)
                .orElseThrow(() ->
                        new RuntimeException("Job not found or access denied"));
    }
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }
    public Job updateJob(Long id, Job updatedJob, String username) {

        Job existingJob = jobRepository.findByIdAndCreatedBy(id, username)
                .orElseThrow(() ->
                        new RuntimeException("Job not found or access denied"));

        // update allowed fields only
        existingJob.setJobName(updatedJob.getJobName());
        existingJob.setDescription(updatedJob.getDescription());
        existingJob.setPayload(updatedJob.getPayload());

        return jobRepository.save(existingJob);
    }

    public void deleteJob(Long id, String username) {

        Job job = jobRepository.findByIdAndCreatedBy(id, username)
                .orElseThrow(() ->
                        new RuntimeException("Job not found or access denied"));

        jobRepository.delete(job);
    }
    public Page<Job> getJobsPaginated(String userName, int page, int size, String sortBy) {
        return jobRepository.findAll(PageRequest.of(page, size, Sort.by(sortBy)));
    }

    public Job cancelJob(Long jobId, String username) {

        Job job = getJobByIdAndUser(jobId, username);

        if (job.getStatus() == JobStatus.COMPLETED ||
                job.getStatus() == JobStatus.CANCELLED) {
            return job; // no-op
        }

        job.setStatus(JobStatus.CANCELLED);
        return jobRepository.save(job);
    }

    // =========================
    // JOB LOGS
    // =========================
    public Page<?> getJobLogs(Long jobId, String username, int page, int size) {

        // ownership validation
        Job job = getJobByIdAndUser(jobId, username);

        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return executionLogRepository.findByJobId(job.getId(), pageable);
    }

    // =========================
    // JOBS BY STATUS
    // =========================
    public List<Job> getJobsByStatus(JobStatus status, String username) {
        return jobRepository.findByStatusAndCreatedBy(status, username);
    }

    // =========================
    // JOB STATISTICS
    // =========================

    public Object getJobStatistics(String username) {

        Map<String, Object> stats = new HashMap<>();

        stats.put("totalJobs", jobRepository.countByCreatedBy(username));

        for (JobStatus status : JobStatus.values()) {
            stats.put(
                    status.name().toLowerCase(),
                    jobRepository.countByStatusAndCreatedBy(status, username)
            );
        }

        return stats;
    }
    @Transactional
    public void executeJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setStatus(JobStatus.RUNNING);
        job.setStartedAt(LocalDateTime.now());
        jobRepository.save(job);

        ExecutionLog log = new ExecutionLog();
        log.setJob(job);
        log.setStartedAt(LocalDateTime.now());

        try {
            // 🔥 YOUR BUSINESS LOGIC
            System.out.println("Executing payload: " + job.getPayload());

            job.setStatus(JobStatus.COMPLETED);
            job.setCompletedAt(LocalDateTime.now());

            log.setStatus(JobStatus.valueOf("SUCCESS"));
        } catch (Exception e) {
            job.setStatus(JobStatus.FAILED);
            job.setErrorMessage(e.getMessage());

            log.setStatus(JobStatus.valueOf("FAILED"));
            log.setErrorMessage(e.getMessage());
        }

        log.setEndedAt(LocalDateTime.now());
        executionLogRepository.save(log);
        jobRepository.save(job);
    }

}
