package org.example.assignment.starwars.services.strategy.components;

import org.example.assignment.data.models.Starship;
import org.example.assignment.data.responses.DataResponseWrapper;
import org.example.assignment.rest.CustomRestClient;
import org.example.assignment.starwars.dtos.responses.ResponseWrapper;
import org.example.assignment.starwars.dtos.responses.StarShipResponse;
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

public class StarShipServiceTest {
    @Mock
    private DataSourceService dataSourceService;

    @Mock
    private CustomRestClient restClient;

    @InjectMocks
    private StarShipService starShipService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Starship createDummyStarShip() {
        Starship starShip = new Starship();
        starShip.setName("Millennium Falcon");
        starShip.setModel("YT-1300 light freighter");
        starShip.setManufacturer("Corellian Engineering Corporation");
        starShip.setCostInCredits("100000");
        starShip.setLength("34.75");
        starShip.setMaxAtmospheringSpeed("1050");
        starShip.setCrew("4");
        starShip.setPassengers("6");
        starShip.setCargoCapacity("100000");
        starShip.setConsumables("2 months");
        starShip.setStarshipClass("Light freighter");
        return starShip;
    }

    @Test
    public void testSearch_Success() {
        DataResponseWrapper<List<Starship>> dataResponseWrapper = new DataResponseWrapper<>();
        dataResponseWrapper.setResults(List.of(createDummyStarShip()));
        dataResponseWrapper.setCount(1);

        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenReturn(dataResponseWrapper);

        ResponseWrapper<List<StarShipResponse>> response = starShipService.search("Millennium Falcon", Optional.empty());

        assertNotNull(response);
        assertEquals(1, response.getResult().size());
        assertEquals("Millennium Falcon", response.getResult().get(0).getName());
    }

    @Test
    public void testSearch_StarShipNotFound() {
        DataResponseWrapper<List<Starship>> dataResponseWrapper = new DataResponseWrapper<>();
        dataResponseWrapper.setResults(List.of());

        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenReturn(dataResponseWrapper);

        assertThrows(ResourceNotFoundException.class, () -> {
            starShipService.search("Nonexistent StarShip", Optional.empty());
        });
    }

    @Test
    public void testSearch_Exception() {
        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        assertThrows(RuntimeException.class, () -> {
            starShipService.search("Millennium Falcon", Optional.empty());
        });
    }
}
