package cronos.scheduler.repo;

import cronos.scheduler.entity.ExecutionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecutionLogRepository extends JpaRepository<ExecutionLog, Long> {

    Page<ExecutionLog> findByJobId(Long jobId, Pageable pageable);
}

