package org.example.assignment.rest.impls;

import org.example.assignment.rest.exceptions.RestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class CustomRestClientImplTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private CustomRestClientImpl customRestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customRestClient = new CustomRestClientImpl("https://swapi.dev/");
    }

    private String getMockData() {
        return "{\"people\":\"https://swapi.dev/api/people/\",\"planets\":\"https://swapi.dev/api/planets/\",\"films\":\"https://swapi.dev/api/films/\",\"species\":\"https://swapi.dev/api/species/\",\"vehicles\":\"https://swapi.dev/api/vehicles/\",\"starships\":\"https://swapi.dev/api/starships/\"}";
    }

    @Test
    void getRequestReturnsExpectedResponse() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(getMockData()));

        String response = customRestClient.getRequest("/api/?format=json", new ParameterizedTypeReference<String>() {});
        assert response.equals(getMockData());
    }

    @Test
    void getRequestThrowsRestExceptionOnError() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenThrow(WebClientResponseException.class);

        assertThrows(RestException.class, () -> customRestClient.getRequest("/test", new ParameterizedTypeReference<String>() {}));
    }

//    @Test
//    void postRequestReturnsExpectedResponse() {
//        when(webClient.post()).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
//        when(requestBodySpec.body(any())).thenReturn(requestHeadersSpec);
//        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just("response"));
//
//        String response = customRestClient.postRequest("/test", "request", new ParameterizedTypeReference<String>() {});
//        assert response.equals("response");
//    }
//
//    @Test
//    void postRequestThrowsRestExceptionOnError() {
//        when(webClient.post()).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
//        when(requestBodySpec.body(any())).thenReturn(requestHeadersSpec);
//        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenThrow(WebClientResponseException.class);
//
//        assertThrows(RestException.class, () -> customRestClient.postRequest("/test", "request", new ParameterizedTypeReference<String>() {}));
//    }
//
//    @Test
//    void putRequestReturnsExpectedResponse() {
//        when(webClient.put()).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
//        when(requestBodySpec.body(any())).thenReturn(requestHeadersSpec);
//        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just("response"));
//
//        String response = customRestClient.putRequest("/test", "request", new ParameterizedTypeReference<String>() {});
//        assert response.equals("response");
//    }
//
//    @Test
//    void putRequestThrowsRestExceptionOnError() {
//        when(webClient.put()).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
//        when(requestBodySpec.body(any())).thenReturn(requestHeadersSpec);
//        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenThrow(WebClientResponseException.class);
//
//        assertThrows(RestException.class, () -> customRestClient.putRequest("/test", "request", new ParameterizedTypeReference<String>() {}));
//    }
}
