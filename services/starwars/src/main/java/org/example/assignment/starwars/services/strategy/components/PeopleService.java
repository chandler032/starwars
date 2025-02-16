package org.example.assignment.starwars.services.strategy.components;

import lombok.extern.slf4j.Slf4j;
import org.example.assignment.data.models.People;
import org.example.assignment.data.responses.DataResponseWrapper;
import org.example.assignment.mapcache.MapCache;
import org.example.assignment.rest.CustomRestClient;
import org.example.assignment.starwars.dtos.responses.PeopleResponse;
import org.example.assignment.starwars.dtos.responses.ResponseWrapper;
import org.example.assignment.starwars.exceptions.ResourceNotFoundException;
import org.example.assignment.starwars.services.external.DataSourceService;
import org.example.assignment.starwars.services.strategy.DataServiceStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("people")
@Slf4j
public class PeopleService implements DataServiceStrategy<PeopleResponse> {
    @Autowired
    private DataSourceService sourceService;

    @Autowired
    private FilmService filmService;

    @Autowired
    @Qualifier("peopleRestClient")
    private CustomRestClient restClient;

    @Override
    public ResponseWrapper<List<PeopleResponse>> search(String searchValue, Optional<String> pageUrl) {
        try {
            log.info("Searching for people with search value: {}", searchValue);
            ResponseWrapper<List<PeopleResponse>> peopleResponseWrapper = new ResponseWrapper<>();
            DataResponseWrapper<List<People>> peopleDataResponseWrapper = null;
            if (pageUrl.isPresent()) {
                String decodedUrl = URLDecoder.decode(pageUrl.get(), StandardCharsets.UTF_8);
                peopleDataResponseWrapper = sourceService.getData(restClient, decodedUrl,
                        new ParameterizedTypeReference<>() {
                        });
            } else {
                peopleDataResponseWrapper = sourceService.getData(restClient, "/?search=" + searchValue,
                        new ParameterizedTypeReference<>() {
                        });
            }

            if (!Objects.isNull(peopleDataResponseWrapper)) {
                if (CollectionUtils.isEmpty(peopleDataResponseWrapper.getResults())) {
                    throw new ResourceNotFoundException("People not found with search value: " + searchValue);
                }
                List<People> peopleList = peopleDataResponseWrapper.getResults();

                List<PeopleResponse> peopleResponses = transferData(peopleList);

                peopleResponseWrapper.setResult(peopleResponses);
                if (Objects.nonNull(peopleDataResponseWrapper.getNext())) {
                    peopleResponseWrapper.setNext(peopleDataResponseWrapper.getNext());
                }

                if (Objects.nonNull(peopleDataResponseWrapper.getPrevious())) {
                    peopleResponseWrapper.setPrevious(peopleDataResponseWrapper.getPrevious());
                }

                if (peopleDataResponseWrapper.getCount() > 0) {
                    peopleResponseWrapper.setCount(peopleDataResponseWrapper.getCount());
                }
            }

            return peopleResponseWrapper;
        } catch (Exception e) {
            log.error("Error occurred while fetching data from external service", e);
            throw e;
        }
    }

    @MapCache
    public PeopleResponse getPeopleByID(String type, String id) {
        try {
            People people = sourceService
                    .getData(restClient, "/" + id + "?format=json",
                            new ParameterizedTypeReference<>() {});

            if (Objects.nonNull(people)) {
                return transferData(List.of(people)).get(0);
            }

            return null;
        } catch (Exception e) {
            log.error("Error occurred while fetching data from external service", e);
            throw e;
        }
    }

    private List<PeopleResponse> transferData(List<People> peopleList) {
        return peopleList.stream().map(people -> {
            PeopleResponse peopleResponse = new PeopleResponse();
            peopleResponse.setName(people.getName());
            peopleResponse.setHeight(people.getHeight());
            peopleResponse.setMass(people.getMass());
            peopleResponse.setHairColor(people.getHairColor());
            peopleResponse.setSkinColor(people.getSkinColor());
            peopleResponse.setEyeColor(people.getEyeColor());
            peopleResponse.setBirthYear(people.getBirthYear());
            peopleResponse.setGender(people.getGender());
            peopleResponse.setHomeworld(people.getHomeworld());
            peopleResponse.setFilms(people.getFilms());
            peopleResponse.setSpecies(people.getSpecies());
            peopleResponse.setVehicles(people.getVehicles());
            peopleResponse.setStarships(people.getStarships());
            peopleResponse.setCreated(people.getCreated());
            peopleResponse.setEdited(people.getEdited());
            peopleResponse.setUrl(people.getUrl());
            return peopleResponse;
        }).toList();
    }
}
