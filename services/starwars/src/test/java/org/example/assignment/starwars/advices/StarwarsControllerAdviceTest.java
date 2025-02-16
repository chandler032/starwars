package org.example.assignment.starwars.advices;

import org.example.assignment.starwars.dtos.responses.ErrorResponse;
import org.example.assignment.starwars.exceptions.InvalidSearchRequestException;
import org.example.assignment.starwars.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StarwarsControllerAdviceTest {
    @InjectMocks
    private StarwarsControllerAdvice starwarsControllerAdvice;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleResourceNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");
        ResponseEntity<ErrorResponse> response = starwarsControllerAdvice.handleResourceNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody().getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
    }

    @Test
    public void testHandleInvalidSearchRequestException() {
        InvalidSearchRequestException exception = new InvalidSearchRequestException("Invalid search request");
        ResponseEntity<ErrorResponse> response = starwarsControllerAdvice.handleInvalidSearchRequestException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid search request", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
    }

    @Test
    public void testHandleException() {
        Exception exception = new Exception("Internal server error");
        ResponseEntity<ErrorResponse> response = starwarsControllerAdvice.handleException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody().getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
    }
}
