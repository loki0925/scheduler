package cronos.scheduler.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmailPayload {
    private List<String> to;
    private String subject;
    private String body;
}

