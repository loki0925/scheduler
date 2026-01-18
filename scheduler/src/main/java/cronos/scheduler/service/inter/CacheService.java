package cronos.scheduler.service.inter;


public interface CacheService {

    void put(String cacheName, String key, Object value, Long ttlSeconds);

    Object get(String cacheName, String key);

    void evict(String cacheName, String key);

    void evictAll(String cacheName);
}

