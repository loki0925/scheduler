package cronos.scheduler.scheduler;


import com.fasterxml.jackson.databind.ObjectMapper;
import cronos.scheduler.dto.HttpJobPayload;
import cronos.scheduler.entity.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class HttpJobExecutor {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public void execute(Job job) throws Exception {

        HttpJobPayload payload =
                objectMapper.readValue(job.getPayload(), HttpJobPayload.class);

        HttpHeaders headers = new HttpHeaders();
        if (payload.getHeaders() != null) {
            payload.getHeaders().forEach(headers::add);
        }

        HttpEntity<Object> entity =
                new HttpEntity<>(payload.getBody(), headers);

        String url = buildUrl(payload);

        HttpMethod method =
                HttpMethod.valueOf(payload.getMethod().toUpperCase());

        ResponseEntity<String> response =
                restTemplate.exchange(url, method, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(
                    "HTTP Job failed with status: " + response.getStatusCode()
            );
        }
    }

    private String buildUrl(HttpJobPayload payload) {

        if (payload.getQueryParams() == null || payload.getQueryParams().isEmpty()) {
            return payload.getUrl();
        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(payload.getUrl());

        for (Map.Entry<String, String> entry : payload.getQueryParams().entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        return builder.toUriString();
    }
}
