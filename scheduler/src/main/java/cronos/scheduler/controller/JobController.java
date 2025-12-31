package cronos.scheduler.controller;

import cronos.scheduler.entity.Job;
import cronos.scheduler.service.JobService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // =========================
    // CREATE JOB
    // =========================
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public Job createJob(@RequestBody @Valid Job job,
                         Authentication authentication) {


        job.setCreatedBy(authentication.getName());

        return jobService.createJob(job);
    }

    // =========================
    // GET ALL JOBS (basic)
    // =========================
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<Job> getAllJobs(Authentication authentication) {
        return jobService.getAllJobsByUser(authentication.getName());
    }

    // =========================
    // GET JOB BY ID
    // =========================
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public Job getJob(@PathVariable Long id,
                      Authentication authentication) {

        return jobService.getJobByIdAndUser(id, authentication.getName());
    }

    // =========================
    // UPDATE JOB
    // =========================
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public Job updateJob(@PathVariable Long id,
                         @RequestBody Job job,
                         Authentication authentication) {

        return jobService.updateJob(id, job, authentication.getName());
    }

    // =========================
    // DELETE JOB
    // =========================
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable Long id,
                          Authentication authentication) {

        jobService.deleteJob(id, authentication.getName());
    }

    // =========================
    // PAGINATION (optional now)
    // =========================
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/paged")
    public Page<Job> getJobsPaged(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(defaultValue = "id") String sortBy,
            Authentication authentication) {

        return jobService.getJobsPaginated(
                authentication.getName(), page, size, sortBy);
    }
}
