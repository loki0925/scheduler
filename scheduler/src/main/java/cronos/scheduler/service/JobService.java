package cronos.scheduler.service;


import cronos.scheduler.entity.Job;
import cronos.scheduler.repo.JobRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class JobService {

    private final JobRepo jobRepository;

    public JobService(JobRepo jobRepository) {
        this.jobRepository = jobRepository;
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
}
