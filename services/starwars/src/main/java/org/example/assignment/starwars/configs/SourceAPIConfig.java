package org.example.assignment.starwars.configs;

import org.example.assignment.rest.CustomRestClient;
import org.example.assignment.rest.impls.CustomRestClientImpl;
import org.example.assignment.starwars.constants.URLConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SourceAPIConfig {
    @Value("${source.api-url}")
    private String sourceBaseUrl;

    @Bean("peopleRestClient")
    public CustomRestClient peopleRestClient() {
        return new CustomRestClientImpl(sourceBaseUrl + URLConstants.PEOPLE_URL);
    }

    @Bean("filmRestClient")
    public CustomRestClient filmRestClient() {
        return new CustomRestClientImpl(sourceBaseUrl + URLConstants.FILMS_URL);
    }

    @Bean("planetRestClient")
    public CustomRestClient planetRestClient() {
        return new CustomRestClientImpl(sourceBaseUrl + URLConstants.PLANETS_URL);
    }

    @Bean("speciesRestClient")
    public CustomRestClient speciesRestClient() {
        return new CustomRestClientImpl(sourceBaseUrl + URLConstants.SPECIES_URL);
    }

    @Bean("starshipRestClient")
    public CustomRestClient starshipRestClient() {
        return new CustomRestClientImpl(sourceBaseUrl + URLConstants.STARSHIPS_URL);
    }

    @Bean("vehicleRestClient")
    public CustomRestClient vehicleRestClient() {
        return new CustomRestClientImpl(sourceBaseUrl + URLConstants.VEHICLES_URL);
    }
}
