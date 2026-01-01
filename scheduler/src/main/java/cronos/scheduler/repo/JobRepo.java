package cronos.scheduler.repo;

import cronos.scheduler.entity.Job;
import cronos.scheduler.entity.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface JobRepo  extends JpaRepository<Job, Long> {



        List<Job> findByCreatedBy(String createdBy);

        Optional<Job> findByIdAndCreatedBy(Long id, String createdBy);

        Page<Job> findByCreatedBy(String createdBy, Pageable pageable);

        List<Job> findByStatusAndCreatedBy(JobStatus status, String createdBy);

        long countByCreatedBy(String createdBy);

        long countByStatusAndCreatedBy(JobStatus status, String createdBy);

}
