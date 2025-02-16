package org.example.assignment.starwars.services.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class DataServiceStrategyFactory {
    @Autowired
    private Map<String, DataServiceStrategy<?>> searchStrategyMap;

    public DataServiceStrategy<?> getSearchStrategy(String type) {
        return searchStrategyMap.get(type);
    }
}
