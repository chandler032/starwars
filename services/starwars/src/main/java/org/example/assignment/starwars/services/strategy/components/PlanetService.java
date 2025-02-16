package org.example.assignment.starwars.services.strategy.components;

import lombok.extern.slf4j.Slf4j;
import org.example.assignment.data.models.Planet;
import org.example.assignment.data.responses.DataResponseWrapper;
import org.example.assignment.rest.CustomRestClient;
import org.example.assignment.starwars.dtos.responses.PlanetResponse;
import org.example.assignment.starwars.dtos.responses.ResponseWrapper;
import org.example.assignment.starwars.exceptions.ResourceNotFoundException;
import org.example.assignment.starwars.services.external.DataSourceService;
import org.example.assignment.starwars.services.strategy.DataServiceStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("planet")
@Slf4j
public class PlanetService implements DataServiceStrategy<PlanetResponse> {
    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    @Qualifier("planetRestClient")
    private CustomRestClient restClient;

    @Override
    public ResponseWrapper<List<PlanetResponse>> search(String searchValue, Optional<String> pageUrl) {
        try {
            ResponseWrapper<List<PlanetResponse>> planetResponseWrapper = new ResponseWrapper<>();

            DataResponseWrapper<List<Planet>> planetDataResponseWrapper = dataSourceService
                    .getData(restClient, "/?search=" + searchValue,
                            new ParameterizedTypeReference<>() {
                            });

            if (planetDataResponseWrapper != null) {
                if (CollectionUtils.isEmpty(planetDataResponseWrapper.getResults())) {
                    throw new ResourceNotFoundException("Planet not found with search value: " + searchValue);
                }
                List<PlanetResponse> planetList = transferPlanetData(planetDataResponseWrapper.getResults());

                planetResponseWrapper.setResult(planetList);

                if (Objects.nonNull(planetDataResponseWrapper.getNext())) {
                    planetResponseWrapper.setNext(planetDataResponseWrapper.getNext());
                }

                if (Objects.nonNull(planetDataResponseWrapper.getPrevious())) {
                    planetResponseWrapper.setPrevious(planetDataResponseWrapper.getPrevious());
                }

                if (planetDataResponseWrapper.getCount() > 0) {
                    planetResponseWrapper.setCount(planetDataResponseWrapper.getCount());
                }
            }

            return planetResponseWrapper;
        } catch (Exception e) {
            log.error("Error occurred while searching for planet: {}", e.getMessage());
            throw e;
        }
    }

    private List<PlanetResponse> transferPlanetData(List<Planet> planetList) {
        return planetList.stream().map(planet -> {
            PlanetResponse planetResponse = new PlanetResponse();
            planetResponse.setName(planet.getName());
            planetResponse.setRotationPeriod(planet.getRotationPeriod());
            planetResponse.setOrbitalPeriod(planet.getOrbitalPeriod());
            planetResponse.setDiameter(planet.getDiameter());
            planetResponse.setClimate(planet.getClimate());
            planetResponse.setGravity(planet.getGravity());
            planetResponse.setTerrain(planet.getTerrain());
            planetResponse.setSurfaceWater(planet.getSurfaceWater());
            planetResponse.setPopulation(planet.getPopulation());
            planetResponse.setResidents(planet.getResidents());
            planetResponse.setFilms(planet.getFilms());
            planetResponse.setCreated(planet.getCreated());
            planetResponse.setEdited(planet.getEdited());
            planetResponse.setUrl(planet.getUrl());
            return planetResponse;
        }).toList();
    }
}
