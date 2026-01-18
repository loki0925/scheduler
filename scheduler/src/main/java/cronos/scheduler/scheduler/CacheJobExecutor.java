package cronos.scheduler.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;

import cronos.scheduler.dto.CacheJobPayload;
import cronos.scheduler.entity.Job;
import cronos.scheduler.service.inter.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheJobExecutor {

    private final CacheService cacheService;
    private final ObjectMapper objectMapper;

    public void execute(Job job) throws Exception {

        CacheJobPayload payload =
                objectMapper.readValue(job.getPayload(), CacheJobPayload.class);

        switch (payload.getOperation()) {

            case "PUT" -> cacheService.put(
                    payload.getCacheName(),
                    payload.getKey(),
                    payload.getValue(),
                    payload.getTtlSeconds()
            );

            case "GET" -> cacheService.get(
                    payload.getCacheName(),
                    payload.getKey()
            );

            case "EVICT" -> cacheService.evict(
                    payload.getCacheName(),
                    payload.getKey()
            );

            case "EVICT_ALL" -> cacheService.evictAll(
                    payload.getCacheName()
            );

            case "REFRESH" -> {
                cacheService.evict(payload.getCacheName(), payload.getKey());
                cacheService.put(
                        payload.getCacheName(),
                        payload.getKey(),
                        payload.getValue(),
                        payload.getTtlSeconds()
                );
            }

            default -> throw new IllegalArgumentException("Unsupported CACHE operation");
        }
    }
}

