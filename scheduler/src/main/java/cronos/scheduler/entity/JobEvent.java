package cronos.scheduler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobEvent {
    private Long jobId;
    private String status;
    private LocalDateTime timestamp;
}
