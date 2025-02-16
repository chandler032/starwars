package org.example.assignment.starwars.services.strategy.components;

import lombok.extern.slf4j.Slf4j;
import org.example.assignment.data.models.Starship;
import org.example.assignment.data.responses.DataResponseWrapper;
import org.example.assignment.rest.CustomRestClient;
import org.example.assignment.starwars.dtos.responses.ResponseWrapper;
import org.example.assignment.starwars.dtos.responses.StarShipResponse;
import org.example.assignment.starwars.exceptions.ResourceNotFoundException;
import org.example.assignment.starwars.services.external.DataSourceService;
import org.example.assignment.starwars.services.strategy.DataServiceStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("starship")
@Slf4j
public class StarShipService implements DataServiceStrategy<StarShipResponse> {
    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    @Qualifier("starshipRestClient")
    private CustomRestClient restClient;

    @Override
    public ResponseWrapper<List<StarShipResponse>> search(String searchValue, Optional<String> pageUrl) {
        try {
            ResponseWrapper<List<StarShipResponse>> starShipResponseWrapper = new ResponseWrapper<>();
            DataResponseWrapper<List<Starship>> starShipDataResponse = dataSourceService
                    .getData(restClient, "/?search=" + searchValue, new ParameterizedTypeReference<>() {
            });

            if (starShipDataResponse != null) {
                if (starShipDataResponse.getResults().isEmpty()) {
                    throw new ResourceNotFoundException("Starship not found with search value: " + searchValue);
                }
                List<Starship> starShipList = starShipDataResponse.getResults();

                List<StarShipResponse> starShipResponses = transferStarShipData(starShipList);

                starShipResponseWrapper.setResult(starShipResponses);

                if (Objects.nonNull(starShipDataResponse.getNext())) {
                    starShipResponseWrapper.setNext(starShipDataResponse.getNext());
                }

                if (Objects.nonNull(starShipDataResponse.getPrevious())) {
                    starShipResponseWrapper.setPrevious(starShipDataResponse.getPrevious());
                }

                if (starShipDataResponse.getCount() > 0) {
                    starShipResponseWrapper.setCount(starShipDataResponse.getCount());
                }
            }

            return starShipResponseWrapper;
        } catch (Exception e) {
            log.error("Error occurred while searching for starship: {}", e.getMessage());
            throw e;
        }
    }

    private List<StarShipResponse> transferStarShipData(List<Starship> starShipList) {
        return starShipList.stream().map(starship -> {
            StarShipResponse starShipResponse = new StarShipResponse();
            starShipResponse.setName(starship.getName());
            starShipResponse.setModel(starship.getModel());
            starShipResponse.setManufacturer(starship.getManufacturer());
            starShipResponse.setCostInCredits(starship.getCostInCredits());
            starShipResponse.setLength(starship.getLength());
            starShipResponse.setMaxAtmospheringSpeed(starship.getMaxAtmospheringSpeed());
            starShipResponse.setCrew(starship.getCrew());
            starShipResponse.setPassengers(starship.getPassengers());
            starShipResponse.setCargoCapacity(starship.getCargoCapacity());
            starShipResponse.setConsumables(starship.getConsumables());
            starShipResponse.setHyperDriveRating(starship.getHyperDriveRating());
            starShipResponse.setMglt(starship.getMglt());
            starShipResponse.setStarshipClass(starship.getStarshipClass());
            starShipResponse.setPilots(starship.getPilots());
            starShipResponse.setFilms(starship.getFilms());
            starShipResponse.setCreated(starship.getCreated());
            starShipResponse.setEdited(starship.getEdited());
            starShipResponse.setUrl(starship.getUrl());
            return starShipResponse;
        }).toList();
    }
}
