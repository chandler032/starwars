package org.example.assignment.rest.exceptions;

public class RestException extends RuntimeException {
    int statusCode;

    public RestException(String message) {
        super(message);
    }

    public RestException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
