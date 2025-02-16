package org.example.assignment.starwars.services.impls;

import org.example.assignment.data.models.Film;
import org.example.assignment.data.models.People;
import org.example.assignment.data.responses.DataResponseWrapper;
import org.example.assignment.mapcache.components.OfflineMode;
import org.example.assignment.rest.CustomRestClient;
import org.example.assignment.starwars.dtos.responses.BaseResponse;
import org.example.assignment.starwars.dtos.responses.FilmResponse;
import org.example.assignment.starwars.dtos.responses.PeopleResponse;
import org.example.assignment.starwars.dtos.responses.ResponseWrapper;
import org.example.assignment.starwars.exceptions.InvalidSearchRequestException;
import org.example.assignment.starwars.services.external.CacheService;
import org.example.assignment.starwars.services.external.DataSourceService;
import org.example.assignment.starwars.services.strategy.DataServiceStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class StarWarsServiceImplTest {
    @Mock
    private DataServiceStrategyFactory strategyFactory;

    @Mock
    private AsyncService asyncService;

    @Mock
    private CacheService cacheService;

    @Mock
    private DataSourceService dataSourceService;

    @InjectMocks
    private StarWarsServiceImpl starWarsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    public PeopleResponse createSamplePeopleResponse() {
        PeopleResponse peopleResponse = new PeopleResponse();
        peopleResponse.setName("Luke Skywalker");
        peopleResponse.setBirthYear("19BBY");
        peopleResponse.setEyeColor("blue");
        peopleResponse.setGender("male");
        peopleResponse.setHairColor("blond");
        peopleResponse.setHeight("172");
        peopleResponse.setMass("77");
        peopleResponse.setSkinColor("fair");
        peopleResponse.setHomeworld("https://swapi.dev/api/planets/1/");
        peopleResponse.setUrl("https://swapi.dev/api/people/1/");
        peopleResponse.setCreated(LocalDateTime.now());
        peopleResponse.setEdited(LocalDateTime.now());
        peopleResponse.setFilms(List.of(
                "https://swapi.dev/api/films/1/"
        ));
        peopleResponse.setStarships(List.of(
                "https://swapi.dev/api/starships/12/",
                "https://swapi.dev/api/starships/22/"
        ));
        peopleResponse.setVehicles(List.of(
                "https://swapi.dev/api/vehicles/14/",
                "https://swapi.dev/api/vehicles/30/"
        ));
        return peopleResponse;
    }

    public People createSamplePeople() {
        People people = new People();
        people.setName("Luke Skywalker");
        people.setBirthYear("19BBY");
        people.setEyeColor("blue");
        people.setGender("male");
        people.setHairColor("blond");
        people.setHeight("172");
        people.setMass("77");
        people.setSkinColor("fair");
        people.setHomeworld("https://swapi.dev/api/planets/1/");
        people.setUrl("https://swapi.dev/api/people/1/");
        people.setCreated(LocalDateTime.now());
        people.setEdited(LocalDateTime.now());
        people.setFilms(List.of(
                "https://swapi.dev/api/films/1/"
        ));
        people.setStarships(List.of(
                "https://swapi.dev/api/starships/12/",
                "https://swapi.dev/api/starships/22/"
        ));
        people.setVehicles(List.of(
                "https://swapi.dev/api/vehicles/14/",
                "https://swapi.dev/api/vehicles/30/"
        ));
        return people;
    }

    private FilmResponse createDummyFilmResponse() {
        FilmResponse film = new FilmResponse();
        film.setTitle("A New Hope");
        film.setEpisodeId(4);
        film.setOpeningCrawl("It is a period of civil war...");
        film.setDirector("George Lucas");
        film.setProducer("Gary Kurtz, Rick McCallum");
        film.setReleaseDate(LocalDate.of(1977, 5, 25));
        film.setCharacters(List.of("Luke Skywalker", "Darth Vader", "Leia Organa"));
        return film;
    }

    private Film createDummyFilm() {
        Film film = new Film();
        film.setTitle("A New Hope");
        film.setEpisodeId(4);
        film.setOpeningCrawl("It is a period of civil war...");
        film.setDirector("George Lucas");
        film.setProducer("Gary Kurtz, Rick McCallum");
        film.setReleaseDate(LocalDate.of(1977, 5, 25));
        film.setCharacters(List.of("Luke Skywalker", "Darth Vader", "Leia Organa"));
        return film;
    }

    @Test
    public void testSearch_SuccessWithPeople() throws ExecutionException, InterruptedException {
        ResponseWrapper<List<PeopleResponse>> responseWrapper = new ResponseWrapper<>();
        List<PeopleResponse> baseResponse = List.of(createSamplePeopleResponse());
        responseWrapper.setResult(baseResponse);

        DataResponseWrapper<List<People>> peopleDataResponseWrapper = new DataResponseWrapper<>();
        peopleDataResponseWrapper.setResults(List.of(createSamplePeople()));
        peopleDataResponseWrapper.setCount(peopleDataResponseWrapper.getResults().size());

        when(dataSourceService
                .getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenReturn(peopleDataResponseWrapper);

        when(asyncService.getFilmData(anyString())).thenReturn(CompletableFuture.completedFuture(Optional.of(createDummyFilmResponse())));

        when(strategyFactory.getSearchStrategy(anyString())).thenReturn((searchValue, pageUrl) -> (ResponseWrapper) responseWrapper);

        starWarsService.enableOfflineMode(false);
        assertFalse(OfflineMode.getInstance().isOfflineModeEnabled());
        ResponseWrapper<? extends List<? extends BaseResponse>> response = starWarsService.search("people", "luke", Optional.empty());

        assertNotNull(response);
        assertEquals(1, response.getResult().size());
        assertEquals("Luke Skywalker", ((PeopleResponse) response.getResult().get(0)).getName());
    }

    @Test
    public void testSearch_SuccessWithFilm() throws ExecutionException, InterruptedException {
        ResponseWrapper<List<FilmResponse>> responseWrapper = new ResponseWrapper<>();
        List<FilmResponse> baseResponse = List.of(createDummyFilmResponse());
        responseWrapper.setResult(baseResponse);

        DataResponseWrapper<List<Film>> filmDataResponseWrapper = new DataResponseWrapper<>();
        filmDataResponseWrapper.setResults(List.of(createDummyFilm()));
        filmDataResponseWrapper.setCount(filmDataResponseWrapper.getResults().size());

        when(dataSourceService
                .getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenReturn(filmDataResponseWrapper);

        when(asyncService.getPeopleData(anyString())).thenReturn(CompletableFuture.completedFuture(Optional.of(createSamplePeopleResponse())));

        when(strategyFactory.getSearchStrategy(anyString())).thenReturn((searchValue, pageUrl) -> (ResponseWrapper) responseWrapper);

        ResponseWrapper<? extends List<? extends BaseResponse>> response = starWarsService.search("film", "new", Optional.empty());

        assertNotNull(response);
        assertEquals(1, response.getResult().size());
        assertEquals("A New Hope", ((FilmResponse) response.getResult().get(0)).getTitle());

        starWarsService.enableOfflineMode(true);
        assertTrue(OfflineMode.getInstance().isOfflineModeEnabled());

        cacheService.saveCache("film_a new hope", response.getResult());

        when(cacheService.getFromCache(anyString(), anyString())).thenReturn(List.of(createDummyFilmResponse()));

        ResponseWrapper<? extends List<? extends BaseResponse>> offlineResponse = starWarsService.search("film", "new", Optional.empty());

        assertNotNull(offlineResponse);
        assertEquals(1, offlineResponse.getResult().size());
        assertEquals("A New Hope", ((FilmResponse) offlineResponse.getResult().get(0)).getTitle());
    }



    @Test
    public void testSearch_InvalidSearchRequestException_EmptyType() {
        assertThrows(InvalidSearchRequestException.class, () -> {
            starWarsService.search("", "searchValue", Optional.empty());
        });
    }

    @Test
    public void testSearch_InvalidSearchRequestException_EmptySearchValue() {
        assertThrows(InvalidSearchRequestException.class, () -> {
            starWarsService.search("type", "", Optional.empty());
        });
    }

    @Test
    public void testSearch_InvalidSearchRequestException_ShortSearchValue() {
        assertThrows(InvalidSearchRequestException.class, () -> {
            starWarsService.search("type", "ab", Optional.empty());
        });
    }

    @Test
    public void testSearch_Exception() {
        when(strategyFactory.getSearchStrategy(anyString())).thenThrow(new RuntimeException("Service unavailable"));

        assertThrows(RuntimeException.class, () -> {
            starWarsService.search("type", "searchValue", Optional.empty());
        });
    }

    @Test
    public void testEnableOfflineMode() {
        starWarsService.enableOfflineMode(true);
        assertTrue(OfflineMode.getInstance().isOfflineModeEnabled());

        starWarsService.enableOfflineMode(false);
        assertFalse(OfflineMode.getInstance().isOfflineModeEnabled());
    }

//    @Test
//    public void testFetchFromCache() {
//        List<Object> cacheDataList = new ArrayList<>();
//        BaseResponse baseResponse = new BaseResponse();
//        baseResponse.setName("Test");
//        cacheDataList.add(baseResponse);
//
//        when(cacheService.getFromCache(anyString(), anyString())).thenReturn(cacheDataList);
//
//        List<BaseResponse> result = starWarsService.fetchFromCache("type", "searchKey");
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals("Test", result.get(0).getName());
//    }
}
