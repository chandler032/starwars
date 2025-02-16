package org.example.assignment.rest.impls;

import io.netty.resolver.DefaultAddressResolverGroup;
import lombok.extern.slf4j.Slf4j;
import org.example.assignment.rest.CustomRestClient;
import org.example.assignment.rest.exceptions.RestException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.netty.http.client.HttpClient;

@Slf4j
public class CustomRestClientImpl implements CustomRestClient {
    private WebClient webClient;
    private HttpClient httpClient;
    private ExchangeStrategies exchangeStrategies;

    public CustomRestClientImpl(String baseUrl) {
        log.info("BaseUrl: {}", baseUrl);
        this.httpClient = HttpClient
                .newConnection()
                .resolver(DefaultAddressResolverGroup.INSTANCE);

        int size = 32 * 1024 * 1024;

        this.exchangeStrategies = ExchangeStrategies.builder()
                .codecs(codec -> codec.defaultCodecs().maxInMemorySize(size))
                .build();

        this.webClient = WebClient.builder().baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

    @Override
    public <T> T getRequest(String url, ParameterizedTypeReference<T> result) throws RestException {
        try {
            log.info("Requesting data from: {}", url);
            return this.webClient.get().uri(url)
                    .retrieve()
                    .bodyToMono(result).block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().is4xxClientError()) {
                throw new RestException("Requested resource "+url+" not found.", e.getStatusCode().value());
            } else if (e.getStatusCode().is5xxServerError()) {
                throw new RestException("Internal server error occurred.", e.getStatusCode().value());
            } else {
                throw new RestException(e.getResponseBodyAsString());
            }
        }
    }

    @Override
    public <T, R> T postRequest(String url, R requestObject, ParameterizedTypeReference<T> result) throws RestException {
        try {
            return this.webClient.post().uri(url)
                    .body(BodyInserters.fromObject(requestObject)).retrieve()
                    .bodyToMono(result).block();
        } catch (WebClientResponseException e) {
            throw new RestException(e.getResponseBodyAsString());
        }
    }

    @Override
    public <T, R> T putRequest(String url, R requestObject, ParameterizedTypeReference<T> result) throws RestException {
        try {
            return this.webClient.put().uri(url)
                    .body(BodyInserters.fromObject(requestObject)).retrieve()
                    .bodyToMono(result).block();
        } catch (WebClientResponseException e) {
            throw new RestException(e.getResponseBodyAsString());
        }
    }
}
