package org.example.assignment.starwars.controllers;

import org.example.assignment.starwars.dtos.responses.ResponseWrapper;
import org.example.assignment.starwars.services.StarWarsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class StarWarsControllerTest {
    @Mock
    private StarWarsService starWarsService;

    @InjectMocks
    private StarWarsController starWarsController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetGreetings() {
        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("hello", "world");

        Map<String, String> response = starWarsController.getGreetings();

        assertEquals(expectedResponse, response);
    }

    @Test
    public void testSearch_Success() throws ExecutionException, InterruptedException {
        ResponseWrapper<?> responseWrapper = new ResponseWrapper<>();
        when(starWarsService.search(anyString(), anyString(), any(Optional.class))).thenReturn(responseWrapper);

        ResponseEntity<ResponseWrapper<?>> response = starWarsController.search("type", "value", Optional.empty());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseWrapper, response.getBody());
    }

    @Test
    public void testSearch_Failure() throws ExecutionException, InterruptedException {
        when(starWarsService.search(anyString(), anyString(), any(Optional.class))).thenThrow(new RuntimeException("Service unavailable"));

        try {
            starWarsController.search("type", "value", Optional.empty());
        } catch (RuntimeException e) {
            assertEquals("Service unavailable", e.getMessage());
        }
    }

    @Test
    public void testEnableOfflineMode() {
        ResponseEntity<?> response = starWarsController.enableOfflineMode(true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
