package org.example.assignment.starwars.services.strategy.components;

import org.example.assignment.data.models.Vehicle;
import org.example.assignment.data.responses.DataResponseWrapper;
import org.example.assignment.rest.CustomRestClient;
import org.example.assignment.starwars.dtos.responses.ResponseWrapper;
import org.example.assignment.starwars.dtos.responses.VehicleResponse;
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

public class VehicleServiceTest {
    @Mock
    private DataSourceService dataSourceService;

    @Mock
    private CustomRestClient restClient;

    @InjectMocks
    private VehicleService vehicleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Vehicle createDummyVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setName("Speeder Bike");
        vehicle.setModel("74-Z");
        vehicle.setManufacturer("Aratech Repulsor Company");
        vehicle.setCostInCredits("8000");
        vehicle.setLength("3.4");
        vehicle.setMaxAtmospheringSpeed("360");
        vehicle.setCrew("1");
        vehicle.setPassengers("1");
        vehicle.setCargoCapacity("4");
        vehicle.setConsumables("1 day");
        vehicle.setVehicleClass("Speeder");
        return vehicle;
    }

    @Test
    public void testSearch_Success() {
        DataResponseWrapper<List<Vehicle>> dataResponseWrapper = new DataResponseWrapper<>();
        dataResponseWrapper.setResults(List.of(createDummyVehicle()));
        dataResponseWrapper.setCount(1);

        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenReturn(dataResponseWrapper);

        ResponseWrapper<List<VehicleResponse>> response = vehicleService.search("Speeder Bike", Optional.empty());

        assertNotNull(response);
        assertEquals(1, response.getResult().size());
        assertEquals("Speeder Bike", response.getResult().get(0).getName());
    }

    @Test
    public void testSearch_VehicleNotFound() {
        DataResponseWrapper<List<Vehicle>> dataResponseWrapper = new DataResponseWrapper<>();
        dataResponseWrapper.setResults(List.of());

        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenReturn(dataResponseWrapper);

        assertThrows(ResourceNotFoundException.class, () -> {
            vehicleService.search("Nonexistent Vehicle", Optional.empty());
        });
    }

    @Test
    public void testSearch_Exception() {
        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        assertThrows(RuntimeException.class, () -> {
            vehicleService.search("Speeder Bike", Optional.empty());
        });
    }
}
