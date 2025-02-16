package org.example.assignment.starwars.services.strategy.components;

import lombok.extern.slf4j.Slf4j;
import org.example.assignment.data.models.Film;
import org.example.assignment.data.responses.DataResponseWrapper;
import org.example.assignment.mapcache.MapCache;
import org.example.assignment.rest.CustomRestClient;
import org.example.assignment.starwars.dtos.responses.FilmResponse;
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

@Service("film")
@Slf4j
public class FilmService implements DataServiceStrategy<FilmResponse> {
    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    @Qualifier("filmRestClient")
    private CustomRestClient restClient;

    @Override
    public ResponseWrapper<List<FilmResponse>> search(String searchValue, Optional<String> pageUrl) {
        try {
            ResponseWrapper<List<FilmResponse>> filmResponseWrapper = new ResponseWrapper<>();

            DataResponseWrapper<List<Film>> filmDataResponseWrapper = null;

            if (pageUrl.isPresent()) {
                filmDataResponseWrapper = dataSourceService
                        .getData(restClient, pageUrl.get(),
                                new ParameterizedTypeReference<>() {});
            } else {
                filmDataResponseWrapper = dataSourceService
                        .getData(restClient, "/?search=" + searchValue,
                                new ParameterizedTypeReference<>() {});
            }

            if (!Objects.isNull(filmDataResponseWrapper)) {
                if (CollectionUtils.isEmpty(filmDataResponseWrapper.getResults())) {
                    throw new ResourceNotFoundException("Film not found with search value: " + searchValue);
                }
                List<Film> filmList = filmDataResponseWrapper.getResults();

                List<FilmResponse> filmResponses = transferFilmData(filmList);
                filmResponseWrapper.setResult(filmResponses);

                if (Objects.nonNull(filmDataResponseWrapper.getNext())) {
                    filmResponseWrapper.setNext(filmDataResponseWrapper.getNext());
                }

                if (Objects.nonNull(filmDataResponseWrapper.getPrevious())) {
                    filmResponseWrapper.setPrevious(filmDataResponseWrapper.getPrevious());
                }

                if (filmDataResponseWrapper.getCount() > 0) {
                    filmResponseWrapper.setCount(filmDataResponseWrapper.getCount());
                }
            }

            return filmResponseWrapper;
        } catch (Exception e) {
            log.error("Error occurred while fetching data from external service", e);
            throw e;
        }
    }

    @MapCache
    public FilmResponse getFilmByID(String type, String id) {
        try {
            Film film = dataSourceService
                    .getData(restClient, "/" + id + "?format=json", new ParameterizedTypeReference<>() {
                    });

            if (Objects.nonNull(film)) {
                return transferFilmData(List.of(film)).get(0);
            }
            return null;
        } catch (Exception e) {
            log.error("Error occurred while fetching data from external service", e);
            throw e;
        }
    }

    private List<FilmResponse> transferFilmData(List<Film> filmList) {
        return filmList.stream().map(film -> {
            FilmResponse filmResponse = new FilmResponse();
            filmResponse.setTitle(film.getTitle());
            filmResponse.setEpisodeId(film.getEpisodeId());
            filmResponse.setOpeningCrawl(film.getOpeningCrawl());
            filmResponse.setDirector(film.getDirector());
            filmResponse.setProducer(film.getProducer());
            filmResponse.setReleaseDate(film.getReleaseDate());
            filmResponse.setCharacters(film.getCharacters());
            filmResponse.setPlanets(film.getPlanets());
            filmResponse.setStarships(film.getStarships());
            filmResponse.setVehicles(film.getVehicles());
            filmResponse.setSpecies(film.getSpecies());
            filmResponse.setCreated(film.getCreated());
            filmResponse.setEdited(film.getEdited());
            filmResponse.setUrl(film.getUrl());
            return filmResponse;
        }).toList();
    }
}
