package org.example.assignment.mapcache.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class MapCacheService {
    private final Map<String, Object> cache = new HashMap<>();

    public Object get(String key) {
        return cache.get(key);
    }

    public void put(String key, Object value) {
        cache.put(key, value);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public Map<String, Object> getMapCache() {
        return cache;
    }

    public List<Object> getFromCache(String type, String searchKey) {
        List<String> keysMatchingType = cache.keySet().stream()
                .filter(k -> k.startsWith(type))
                .toList();
        log.info("Keys matching type: {}. Keys: {}", type, keysMatchingType);
        List<String> keysMatchingSearchKey = keysMatchingType.stream()
                .filter(k -> k.contains(searchKey))
                .toList();
        log.info("Keys matching search key: {}. Keys: {}", searchKey, keysMatchingSearchKey);
        return keysMatchingSearchKey.stream()
                .map(cache::get)
                .toList();
    }
}
