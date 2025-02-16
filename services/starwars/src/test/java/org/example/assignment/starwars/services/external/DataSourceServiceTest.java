package org.example.assignment.starwars.services.external;

import org.example.assignment.rest.CustomRestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class DataSourceServiceTest {
    @Mock
    private CustomRestClient restClient;

    @InjectMocks
    private DataSourceService dataSourceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetData_ApiCallFailure() {
        String url = "http://localhost/api";
        ParameterizedTypeReference<Object> responseType = new ParameterizedTypeReference<>() {};
        doThrow(new RuntimeException("API call failed")).when(restClient).getRequest(anyString(), eq(responseType));
        assertThrows(RuntimeException.class, () -> dataSourceService.getData(restClient, url, responseType));
    }

    @Test
    public void testGetData_Success() {
        String url = "http://localhost/api";
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<>() {};
        String expectedResponse = "Success";
        when(restClient.getRequest(anyString(), eq(responseType))).thenReturn(expectedResponse);
        String actualResponse = dataSourceService.getData(restClient, url, responseType);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }
}
