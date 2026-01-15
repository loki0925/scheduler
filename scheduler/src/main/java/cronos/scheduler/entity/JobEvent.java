package cronos.scheduler.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobEvent {

    private Long jobId;
    private String jobName;
    private String eventType;   // JOB_STARTED, JOB_FAILED, etc
    private String status;
    private String message;
    private LocalDateTime timestamp;
}

