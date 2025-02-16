package org.example.assignment.starwars.services.strategy.components;

import org.example.assignment.data.models.Film;
import org.example.assignment.data.responses.DataResponseWrapper;
import org.example.assignment.rest.CustomRestClient;
import org.example.assignment.starwars.dtos.responses.FilmResponse;
import org.example.assignment.starwars.dtos.responses.ResponseWrapper;
import org.example.assignment.starwars.exceptions.ResourceNotFoundException;
import org.example.assignment.starwars.services.external.DataSourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class FilmServiceTest {
    @Mock
    private DataSourceService dataSourceService;

    @Mock
    private CustomRestClient restClient;

    @InjectMocks
    private FilmService filmService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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
    public void testSearch_Success() {
        DataResponseWrapper<List<Film>> dataResponseWrapper = new DataResponseWrapper<>();
        dataResponseWrapper.setResults(List.of(createDummyFilm()));
        dataResponseWrapper.setCount(1);

        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenReturn(dataResponseWrapper);

        ResponseWrapper<List<FilmResponse>> response = filmService.search("A New Hope", Optional.empty());

        assertNotNull(response);
        assertEquals(1, response.getResult().size());
        assertEquals("A New Hope", response.getResult().get(0).getTitle());
    }

    @Test
    public void testSearch_FilmNotFound() {
        DataResponseWrapper<List<Film>> dataResponseWrapper = new DataResponseWrapper<>();
        dataResponseWrapper.setResults(List.of());

        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenReturn(dataResponseWrapper);

        assertThrows(ResourceNotFoundException.class, () -> {
            filmService.search("Nonexistent Film", Optional.empty());
        });
    }

    @Test
    public void testSearch_Exception() {
        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        assertThrows(RuntimeException.class, () -> {
            filmService.search("A New Hope", Optional.empty());
        });
    }

    @Test
    public void testGetFilmByID_Success() {
        Film film = createDummyFilm();

        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenReturn(film);

        FilmResponse response = filmService.getFilmByID("film", "1");

        assertNotNull(response);
        assertEquals("A New Hope", response.getTitle());
    }

    @Test
    public void testGetFilmByID_FilmNotFound() {
        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenReturn(null);

        FilmResponse response = filmService.getFilmByID("film", "999");

        assertNull(response);
    }

    @Test
    public void testGetFilmByID_Exception() {
        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        assertThrows(RuntimeException.class, () -> {
            filmService.getFilmByID("film", "1");
        });
    }
}
