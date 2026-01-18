package cronos.scheduler.dto;

import lombok.Data;

import java.util.Map;

@Data
public class HttpJobPayload {
    private String url;
    private String method;
    private Map<String, String> headers;
    private Map<String, String> queryParams;
    private Object body;
    private Integer timeoutMs;
}

