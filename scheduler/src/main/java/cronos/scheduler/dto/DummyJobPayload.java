package cronos.scheduler.dto;



import lombok.Data;

@Data
public class DummyJobPayload {

    private String message;
    private long sleepMillis;
    private boolean shouldFail;
}

