package cronos.scheduler.scheduler;


import com.fasterxml.jackson.databind.ObjectMapper;
import cronos.scheduler.dto.DbToKafkaJobPayload;
import cronos.scheduler.entity.Job;
import cronos.scheduler.service.inter.DatabaseAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DbToKafkaJobExecutor {

    private final DatabaseAccessService databaseAccessService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public void execute(Job job) throws Exception {

        DbToKafkaJobPayload payload =
                objectMapper.readValue(job.getPayload(), DbToKafkaJobPayload.class);

        List<Map<String, Object>> rows =
                databaseAccessService.fetchBatch(
                        payload.getSql(),
                        payload.getBatchSize(),
                        payload.getLastProcessedValue()
                );

        for (Map<String, Object> row : rows) {

            String key = payload.getKeyColumn() != null
                    ? String.valueOf(row.get(payload.getKeyColumn()))
                    : null;

            kafkaTemplate.send(payload.getTopic(), key, row);

            payload.setLastProcessedValue(
                    row.get(payload.getIncrementalColumn())
            );
        }

        // Persist checkpoint
        job.setPayload(objectMapper.writeValueAsString(payload));
    }
}
