package org.example.assignment.starwars.services.impls;

import lombok.extern.slf4j.Slf4j;
import org.example.assignment.starwars.dtos.responses.*;
import org.example.assignment.starwars.services.external.CacheService;
import org.example.assignment.starwars.services.strategy.components.FilmService;
import org.example.assignment.starwars.services.strategy.components.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AsyncService {

    @Autowired
    private FilmService filmService;

    @Autowired
    private PeopleService peopleService;

    @Autowired
    @Qualifier("asyncTheadPool")
    ThreadPoolTaskExecutor asyncTheadPool;

    @Async("asyncTheadPool")
    public CompletableFuture<Optional<FilmResponse>> getFilmData(String filmUrl) {
        log.info("Fetching film data from: {}. Thread: {}", filmUrl, Thread.currentThread().getName());
        try {
            String[] urlParts = filmUrl.split("/");
            String filmId = urlParts[urlParts.length - 1];
            Optional<FilmResponse> filmResponseOptional = Optional.ofNullable(filmService.getFilmByID("film", filmId));
            return CompletableFuture.completedFuture(filmResponseOptional);
        } catch (Exception e) {
            log.error("Error occurred while fetching film data", e);
            throw e;
        }
    }

    @Async("asyncTheadPool")
    public CompletableFuture<Void> addToCache(String key, List<? extends BaseResponse> responseList, CacheService cacheService) {
        try {
            if (!CollectionUtils.isEmpty(responseList)) {
                switch (key) {
                    case "film":
                        List<FilmResponse> filmResponseList = responseList
                                .stream()
                                .map(FilmResponse.class::cast)
                                .toList();
                        filmResponseList.forEach(
                                filmResponse -> cacheService
                                        .saveCache("film_" + filmResponse.getTitle().toLowerCase(), filmResponse));
                        break;
                    case "people":
                        List<PeopleResponse> peopleResponseList = responseList
                                .stream()
                                .map(PeopleResponse.class::cast)
                                .toList();
                        peopleResponseList.forEach(
                                peopleResponse -> cacheService
                                        .saveCache("people_" + peopleResponse.getName().toLowerCase(), peopleResponse));
                        break;
                    case "starship":
                        List<StarShipResponse> starShipResponseList = responseList
                                .stream()
                                .map(StarShipResponse.class::cast)
                                .toList();
                        starShipResponseList.forEach(
                                starShipResponse -> cacheService
                                        .saveCache("starship_" + starShipResponse.getName().toLowerCase(), starShipResponse));
                        break;
                    case "vehicle":
                        List<VehicleResponse> vehicleResponseList = responseList
                                .stream()
                                .map(VehicleResponse.class::cast)
                                .toList();
                        vehicleResponseList.forEach(
                                vehicleResponse -> cacheService
                                        .saveCache("vehicle_" + vehicleResponse.getName().toLowerCase(), vehicleResponse));
                        break;
                    case "species":
                        List<SpeciesResponse> speciesResponseList = responseList
                                .stream()
                                .map(SpeciesResponse.class::cast)
                                .toList();
                        speciesResponseList.forEach(
                                speciesResponse -> cacheService
                                        .saveCache("species_" + speciesResponse.getName().toLowerCase(), speciesResponse));
                        break;
                    case "planet":
                        List<PlanetResponse> planetResponseList = responseList
                                .stream()
                                .map(PlanetResponse.class::cast)
                                .toList();
                        planetResponseList.forEach(
                                planetResponse -> cacheService
                                        .saveCache("planet_" + planetResponse.getName().toLowerCase(), planetResponse));
                        break;
                    default:
                        log.error("Invalid key: {}", key);
                }
            }
        } catch (RuntimeException e) {
            log.error("Error occurred while adding to cache", e);
            throw e;
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async("asyncTheadPool")
    public CompletableFuture<Optional<PeopleResponse>> getPeopleData(String peopleUrl) {
        log.info("Fetching people data from: {}. Thread: {}", peopleUrl, Thread.currentThread().getName());
        try {
            String[] urlParts = peopleUrl.split("/");
            String peopleId = urlParts[urlParts.length - 1];
            Optional<PeopleResponse> peopleResponseOptional = Optional.ofNullable(peopleService.getPeopleByID("people", peopleId));
            return CompletableFuture.completedFuture(peopleResponseOptional);
        } catch (Exception e) {
            log.error("Error occurred while fetching people data", e);
            throw e;
        }
    }
}
