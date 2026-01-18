package cronos.scheduler.dto;


import lombok.Data;

@Data
public class DbToKafkaJobPayload {

    private String sql;                // SELECT query
    private String topic;              // Kafka topic
    private int batchSize;             // e.g. 500
    private String keyColumn;          // Optional Kafka key
    private String incrementalColumn;  // created_at / updated_at / id
    private Object lastProcessedValue; // checkpoint
}

