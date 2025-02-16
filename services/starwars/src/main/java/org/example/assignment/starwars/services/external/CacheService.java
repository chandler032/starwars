package org.example.assignment.starwars.services.external;

import lombok.extern.slf4j.Slf4j;
import org.example.assignment.mapcache.components.MapCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CacheService {
    @Autowired
    private MapCacheService mapCacheService;

    public void saveCache(String key, Object value) {
        log.info("Saving cache for key: {}", key);
        mapCacheService.put(key, value);
    }

    public List<Object> getFromCache(String type, String searchKey) {
        return mapCacheService.getFromCache(type, searchKey);
    }
}
