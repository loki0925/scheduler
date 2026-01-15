package cronos.scheduler.service;

import cronos.scheduler.entity.JobEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobEventProducer {

    @Autowired
    private final KafkaTemplate<String, JobEvent> kafkaTemplate;

    private static final String TOPIC = "job-events";

    public void publish(JobEvent event) {
        kafkaTemplate.send(TOPIC, event.getJobId().toString(), event);
    }
}

