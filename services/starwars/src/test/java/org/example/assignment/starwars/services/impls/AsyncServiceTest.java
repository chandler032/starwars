package org.example.assignment.starwars.services.impls;

import org.example.assignment.starwars.dtos.responses.FilmResponse;
import org.example.assignment.starwars.services.external.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

public class AsyncServiceTest {
    @Mock
    private CacheService cacheService;

    @InjectMocks
    private AsyncService asyncService;

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
    public void testAddToCache_Success() {
        List<FilmResponse> filmResponseList = List.of(dummyFilmResponse());
        CompletableFuture<Void> future = asyncService.addToCache("film", filmResponseList, cacheService);
        assertDoesNotThrow(() -> future.get());
        verify(cacheService, times(1)).saveCache(anyString(), any(FilmResponse.class));
    }

    @Test
    public void testAddToCache_InvalidKey() {
        List<FilmResponse> filmResponseList = List.of(new FilmResponse());
        CompletableFuture<Void> future = asyncService.addToCache("invalid_key", filmResponseList, cacheService);
        assertDoesNotThrow(() -> future.get());
        verify(cacheService, never()).saveCache(anyString(), any());
    }

    @Test
    public void testAddToCache_EmptyResponseList() {
        List<FilmResponse> emptyList = List.of();
        CompletableFuture<Void> future = asyncService.addToCache("film", emptyList, cacheService);
        assertDoesNotThrow(() -> future.get());
        verify(cacheService, never()).saveCache(anyString(), any());
    }

    @Test
    public void testAddToCache_NullResponseList() {
        CompletableFuture<Void> future = asyncService.addToCache("film", null, cacheService);
        assertDoesNotThrow(() -> future.get());
        verify(cacheService, never()).saveCache(anyString(), any());
    }

//    @Test
//    public void testAddToCache_ExceptionThrown() {
//        List<FilmResponse> filmResponseList = List.of(dummyFilmResponse());
//        doThrow(new RuntimeException("Cache service error")).when(cacheService).saveCache(anyString(), any(FilmResponse.class));
//        CompletableFuture<Void> future = asyncService.addToCache("film", filmResponseList, cacheService);
//        assertThrows(RuntimeException.class, future::get);
//    }
}
