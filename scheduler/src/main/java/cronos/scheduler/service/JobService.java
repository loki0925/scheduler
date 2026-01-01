package cronos.scheduler.service;


import cronos.scheduler.entity.Job;
import cronos.scheduler.entity.enums.JobStatus;
import cronos.scheduler.repo.ExecutionLogRepository;
import cronos.scheduler.repo.JobRepo;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobService {

    private final JobRepo jobRepository;

    private final ExecutionLogRepository executionLogRepository;

    public JobService(JobRepo jobRepository, ExecutionLogRepository executionLogRepository) {
        this.jobRepository = jobRepository;
        this.executionLogRepository = executionLogRepository;
    }

    public Job createJob(Job job) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        job.setCreatedBy(username);
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
}
