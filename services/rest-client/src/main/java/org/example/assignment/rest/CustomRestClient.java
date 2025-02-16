package org.example.assignment.rest;

import org.example.assignment.rest.exceptions.RestException;
import org.springframework.core.ParameterizedTypeReference;

public interface CustomRestClient {
    <T> T getRequest(String url, ParameterizedTypeReference<T> result) throws RestException;

    <T, R> T postRequest(String url, R requestObject, ParameterizedTypeReference<T> result) throws RestException;

    <T, R> T putRequest(String url, R requestObject, ParameterizedTypeReference<T> result) throws RestException;
}
