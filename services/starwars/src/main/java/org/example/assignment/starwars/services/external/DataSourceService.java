package org.example.assignment.starwars.services.external;

import lombok.extern.slf4j.Slf4j;
import org.example.assignment.rest.CustomRestClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataSourceService {
    public <T> T getData(CustomRestClient restClient, String url,
                         ParameterizedTypeReference<T> result) {
        try {
            return restClient.getRequest(url + "&format=json", result);
        } catch (Exception e) {
            log.error("Error occurred while fetching data from external service", e);
            throw e;
        }
    }
}
