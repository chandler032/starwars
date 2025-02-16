package org.example.assignment.starwars.services.external;

import org.example.assignment.mapcache.components.MapCacheService;
import org.example.assignment.starwars.dtos.responses.FilmResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class CacheServiceTest {
    @Mock
    private MapCacheService mapCacheService;

    @InjectMocks
    private CacheService cacheService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private FilmResponse dummyFilmResponse() {
        FilmResponse filmResponse = new FilmResponse();
        filmResponse.setTitle("A New Hope");
        filmResponse.setDirector("George Lucas");
        filmResponse.setProducer("Gary Kurtz, Rick McCallum");
        filmResponse.setReleaseDate(LocalDate.now());
        filmResponse.setCharacters(List.of("Luke Skywalker", "Darth Vader", "Leia Organa"));

        return filmResponse;
    }

    @Test
    public void testSaveCache_success() {
        cacheService.saveCache("key", "value");
    }

    @Test
    public void testGetFromCache_NullResult() {
        String type = "film";
        String searchKey = "Nonexistent";
        when(mapCacheService.getFromCache(anyString(), anyString())).thenReturn(null);
        List<Object> actualResponse = cacheService.getFromCache(type, searchKey);
        assertEquals(null, actualResponse);
    }

    @Test
    public void testGetFromCache_ReturnsList() {
        String type = "film";
        String searchKey = "A New Hope";
        List<Object> expectedResponse = List.of(dummyFilmResponse());
        when(mapCacheService.getFromCache(anyString(), anyString())).thenReturn(expectedResponse);
        List<Object> actualResponse = cacheService.getFromCache(type, searchKey);
        assertEquals(expectedResponse, actualResponse);
    }
}
