package cronos.scheduler.repo;

import cronos.scheduler.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface JobRepo  extends JpaRepository<Job, Long> {

    List<Job> findByCreatedBy(String createdBy);

    // ✅ for GET BY ID with ownership
    Optional<Job> findByIdAndCreatedBy(Long id, String createdBy);
}
