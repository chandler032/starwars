package org.example.assignment.starwars.services.strategy.components;

import org.example.assignment.data.models.People;
import org.example.assignment.data.responses.DataResponseWrapper;
import org.example.assignment.rest.CustomRestClient;
import org.example.assignment.starwars.dtos.responses.PeopleResponse;
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

public class PeopleServiceTest {
    @Mock
    private DataSourceService dataSourceService;

    @Mock
    private CustomRestClient restClient;

    @InjectMocks
    private PeopleService peopleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private People createDummyPeople() {
        People people = new People();
        people.setName("Luke Skywalker");
        people.setHeight("172");
        people.setMass("77");
        people.setHairColor("blond");
        people.setSkinColor("fair");
        people.setEyeColor("blue");
        people.setBirthYear("19BBY");
        people.setGender("male");
        return people;
    }

    @Test
    public void testSearch_Success() {
        DataResponseWrapper<List<People>> dataResponseWrapper = new DataResponseWrapper<>();
        dataResponseWrapper.setResults(List.of(createDummyPeople()));
        dataResponseWrapper.setCount(1);

        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenReturn(dataResponseWrapper);

        ResponseWrapper<List<PeopleResponse>> response = peopleService.search("Luke Skywalker", Optional.empty());

        assertNotNull(response);
        assertEquals(1, response.getResult().size());
        assertEquals("Luke Skywalker", response.getResult().get(0).getName());
    }

    @Test
    public void testSearch_PeopleNotFound() {
        DataResponseWrapper<List<People>> dataResponseWrapper = new DataResponseWrapper<>();
        dataResponseWrapper.setResults(List.of());

        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenReturn(dataResponseWrapper);

        assertThrows(ResourceNotFoundException.class, () -> {
            peopleService.search("Nonexistent People", Optional.empty());
        });
    }

    @Test
    public void testSearch_Exception() {
        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        assertThrows(RuntimeException.class, () -> {
            peopleService.search("Luke Skywalker", Optional.empty());
        });
    }

    @Test
    public void testGetPeopleByID_Success() {
        People people = createDummyPeople();

        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenReturn(people);

        PeopleResponse response = peopleService.getPeopleByID("people", "1");

        assertNotNull(response);
        assertEquals("Luke Skywalker", response.getName());
    }

    @Test
    public void testGetPeopleByID_PeopleNotFound() {
        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenReturn(null);

        PeopleResponse response = peopleService.getPeopleByID("people", "999");

        assertNull(response);
    }

    @Test
    public void testGetPeopleByID_Exception() {
        when(dataSourceService.getData(any(CustomRestClient.class), anyString(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        assertThrows(RuntimeException.class, () -> {
            peopleService.getPeopleByID("people", "1");
        });
    }
}
