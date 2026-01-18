package cronos.scheduler.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import cronos.scheduler.dto.DatabaseJobPayload;
import cronos.scheduler.entity.Job;
import cronos.scheduler.service.inter.DatabaseAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DatabaseJobExecutor {

    private final DatabaseAccessService databaseService;
    private final ObjectMapper objectMapper;

    @Transactional
    public void execute(Job job) throws Exception {

        DatabaseJobPayload payload =
                objectMapper.readValue(job.getPayload(), DatabaseJobPayload.class);

        switch (payload.getOperation()) {

            case "SELECT" -> databaseService.executeSelect(
                    payload.getSql(),
                    payload.getTimeoutSeconds()
            );

            case "INSERT", "UPDATE", "DELETE" -> databaseService.executeUpdate(
                    payload.getSql(),
                    payload.getTimeoutSeconds()
            );

            case "PROCEDURE" -> databaseService.executeProcedure(
                    payload.getSql(),
                    payload.getTimeoutSeconds()
            );

            case "SCRIPT" -> databaseService.executeScript(
                    payload.getSql(),
                    payload.getTimeoutSeconds()
            );

            default -> throw new IllegalArgumentException("Unsupported DATABASE operation");
        }
    }
}

