package cronos.scheduler.scheduler;


import com.fasterxml.jackson.databind.ObjectMapper;
import cronos.scheduler.dto.DummyJobPayload;
import cronos.scheduler.entity.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DummyJobExecutor {

    private final ObjectMapper objectMapper;

    public void execute(Job job) throws Exception {

        DummyJobPayload payload =
                objectMapper.readValue(job.getPayload(), DummyJobPayload.class);

        if (payload.getSleepMillis() > 0) {
            Thread.sleep(payload.getSleepMillis());
        }

        System.out.println("🟢 DUMMY JOB EXECUTED");
        System.out.println("Message: " + payload.getMessage());

        if (payload.isShouldFail()) {
            throw new RuntimeException("Dummy job forced failure");
        }
    }
}

