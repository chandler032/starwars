package org.example.assignment.starwars.services.strategy.components;

import lombok.extern.slf4j.Slf4j;
import org.example.assignment.data.models.Species;
import org.example.assignment.data.responses.DataResponseWrapper;
import org.example.assignment.rest.CustomRestClient;
import org.example.assignment.starwars.dtos.responses.ResponseWrapper;
import org.example.assignment.starwars.dtos.responses.SpeciesResponse;
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

@Service("species")
@Slf4j
public class SpeciesService implements DataServiceStrategy<SpeciesResponse> {
    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    @Qualifier("speciesRestClient")
    private CustomRestClient restClient;

    @Override
    public ResponseWrapper<List<SpeciesResponse>> search(String searchValue, Optional<String> pageUrl) {
        try {
            ResponseWrapper<List<SpeciesResponse>> speciesResponseWrapper = new ResponseWrapper<>();

            DataResponseWrapper<List<Species>> speciesDataResponseWrapper = dataSourceService
                    .getData(restClient, "/?search=" + searchValue,
                            new ParameterizedTypeReference<>() {
                            });

            if (!Objects.isNull(speciesDataResponseWrapper)) {
                if (CollectionUtils.isEmpty(speciesDataResponseWrapper.getResults())) {
                    throw new ResourceNotFoundException("Species not found with search value: " + searchValue);
                }
                List<Species> speciesList = speciesDataResponseWrapper.getResults();

                List<SpeciesResponse> speciesResponses = transferSpeciesData(speciesList);

                speciesResponseWrapper.setResult(speciesResponses);

                if (Objects.nonNull(speciesDataResponseWrapper.getNext())) {
                    speciesResponseWrapper.setNext(speciesDataResponseWrapper.getNext());
                }

                if (Objects.nonNull(speciesDataResponseWrapper.getPrevious())) {
                    speciesResponseWrapper.setPrevious(speciesDataResponseWrapper.getPrevious());
                }

                if (speciesDataResponseWrapper.getCount() > 0) {
                    speciesResponseWrapper.setCount(speciesDataResponseWrapper.getCount());
                }
            }

            return speciesResponseWrapper;
        } catch (Exception e) {
            log.error("Error occurred while fetching species by search value: {}", e.getMessage());
            throw e;
        }
    }

    private List<SpeciesResponse> transferSpeciesData(List<Species> speciesList) {
        return speciesList.stream().map(species -> {
            SpeciesResponse speciesResponse = new SpeciesResponse();
            speciesResponse.setName(species.getName());
            speciesResponse.setClassification(species.getClassification());
            speciesResponse.setDesignation(species.getDesignation());
            speciesResponse.setAverageHeight(species.getAverageHeight());
            speciesResponse.setSkinColors(species.getSkinColors());
            speciesResponse.setHairColors(species.getHairColors());
            speciesResponse.setEyeColors(species.getEyeColors());
            speciesResponse.setAverageLifespan(species.getAverageLifespan());
            speciesResponse.setHomeworld(species.getHomeworld());
            speciesResponse.setLanguage(species.getLanguage());
            speciesResponse.setPeople(species.getPeople());
            speciesResponse.setFilms(species.getFilms());
            speciesResponse.setCreated(species.getCreated());
            speciesResponse.setEdited(species.getEdited());
            speciesResponse.setUrl(species.getUrl());
            return speciesResponse;
        }).toList();
    }
}
