package cronos.scheduler.service;


import cronos.scheduler.service.inter.CacheService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryCacheService implements CacheService {

    private static class CacheEntry {
        Object value;
        Instant expiry;
    }

    private final Map<String, Map<String, CacheEntry>> cache = new ConcurrentHashMap<>();

    @Override
    public void put(String cacheName, String key, Object value, Long ttlSeconds) {
        cache.computeIfAbsent(cacheName, k -> new ConcurrentHashMap<>());
        CacheEntry entry = new CacheEntry();
        entry.value = value;
        entry.expiry = ttlSeconds != null
                ? Instant.now().plusSeconds(ttlSeconds)
                : null;

        cache.get(cacheName).put(key, entry);
    }

    @Override
    public Object get(String cacheName, String key) {
        CacheEntry entry = cache.getOrDefault(cacheName, Map.of()).get(key);
        if (entry == null) return null;

        if (entry.expiry != null && Instant.now().isAfter(entry.expiry)) {
            evict(cacheName, key);
            return null;
        }
        return entry.value;
    }

    @Override
    public void evict(String cacheName, String key) {
        Map<String, CacheEntry> map = cache.get(cacheName);
        if (map != null) map.remove(key);
    }

    @Override
    public void evictAll(String cacheName) {
        cache.remove(cacheName);
    }
}

