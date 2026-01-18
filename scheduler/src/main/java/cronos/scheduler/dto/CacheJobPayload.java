package cronos.scheduler.dto;

import lombok.Data;

@Data
public class CacheJobPayload {

    private String operation;   // PUT, GET, EVICT, EVICT_ALL, REFRESH
    private String cacheName;
    private String key;
    private Object value;
    private Long ttlSeconds;    // optional
}

