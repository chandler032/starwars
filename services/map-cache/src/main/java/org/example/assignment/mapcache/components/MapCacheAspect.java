package org.example.assignment.mapcache.components;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class MapCacheAspect {
    @Autowired
    private MapCacheService mapCacheService;

    @Around("@annotation(org.example.assignment.mapcache.MapCache)")
    public Object cacheData(ProceedingJoinPoint joinPoint) throws Throwable {
        String cacheKey = generateCacheKey(joinPoint.getArgs());

        if (mapCacheService.containsKey(cacheKey)) {
            log.info("Cache for key found: {}", cacheKey);
            return mapCacheService.get(cacheKey);
        }

        log.info("Cache for key not found: {}", cacheKey);
        Object returnedObject = joinPoint.proceed();
        mapCacheService.put(cacheKey, returnedObject);
        return returnedObject;
    }

    private String generateCacheKey(Object[] args) {
        if (args == null || args.length == 0) {
            return "key";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(args[0].toString());
        for (int i = 1; i < 2; i++) {
            builder.append("_").append(args[i].toString());
        }

        return builder.toString();
    }
}
