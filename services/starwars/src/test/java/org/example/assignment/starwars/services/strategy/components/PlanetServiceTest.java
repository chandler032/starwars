package org.example.assignment.starwars.services.strategy.components;

import org.example.assignment.data.models.Planet;
import org.example.assignment.data.responses.DataResponseWrapper;
import org.example.assignment.rest.CustomRestClient;
import org.example.assignment.starwars.dtos.responses.PlanetResponse;
import org.example.assignment.starwars.dtos.responses.ResponseWrapper;
import org.example.assignment.starwars.exceptions.ResourceNotFoundException;
import org.example.assignment.starwars.services.external.DataSourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class PlanetServiceTest {
    @Mock
    private DataSourceService dataSourceService;

    @Mock
    private CustomRestClient restClient;

    @InjectMocks
    private PlanetService planetService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Planet createDummyPlanet() {
        Planet planet = new Planet();
        planet.setName("Tatooine");
        planet.setRotationPeriod("23");
        planet.setOrbitalPeriod("304");
        planet.setDiameter("10465");
        planet.setClimate("arid");
        planet.setGravity("1 standard");
        planet.setTerrain("desert");
        planet.setSurfaceWater("1");
        planet.setPopulation("200000");
        return planet;
    }

    @Test
    public void testSearch_Success() {
        DataResponseWrapper<List<Planet>> dataResponseWrapper = new DataResponseWrapper<>();
        dataResponseWrapper.setResults(List.of(createDummyPlanet()));
        dataResponseWrapper.setCount(1);

        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenReturn(dataResponseWrapper);

        ResponseWrapper<List<PlanetResponse>> response = planetService.search("Tatooine", Optional.empty());

        assertNotNull(response);
        assertEquals(1, response.getResult().size());
        assertEquals("Tatooine", response.getResult().get(0).getName());
    }

    @Test
    public void testSearch_PlanetNotFound() {
        DataResponseWrapper<List<Planet>> dataResponseWrapper = new DataResponseWrapper<>();
        dataResponseWrapper.setResults(List.of());

        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenReturn(dataResponseWrapper);

        assertThrows(ResourceNotFoundException.class, () -> {
            planetService.search("Nonexistent Planet", Optional.empty());
        });
    }

    @Test
    public void testSearch_Exception() {
        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        assertThrows(RuntimeException.class, () -> {
            planetService.search("Tatooine", Optional.empty());
        });
    }
}
