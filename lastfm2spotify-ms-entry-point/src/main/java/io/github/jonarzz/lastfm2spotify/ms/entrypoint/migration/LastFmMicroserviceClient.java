package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import io.github.jonarzz.lastfm2spotify.commons.dto.LovedTrack;
import io.github.jonarzz.lastfm2spotify.commons.error.ExternalApiUnavailableException;
import io.github.jonarzz.lastfm2spotify.commons.error.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class LastFmMicroserviceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(LastFmMicroserviceClient.class);

    private WebClient lastFmMsClient;

    LastFmMicroserviceClient(WebClient lastFmMsClient) {
        this.lastFmMsClient = lastFmMsClient;
    }

    Flux<LovedTrack> getLovedTracks(String lastFmUsername) {
        return lastFmMsClient.get()
                             .uri("tracks/loved/{lastFmUsername}", lastFmUsername)
                             .retrieve()
                             .onStatus(HttpStatus.NOT_FOUND::equals,
                                       response -> handleClientError(response, new ResourceNotFoundException("User with name " + lastFmUsername + " not found")))
                             .onStatus(HttpStatus.BAD_GATEWAY::equals,
                                       response -> handleClientError(response, new ExternalApiUnavailableException("LastFM API is not unavailable at the moment")))
                             .bodyToFlux(LovedTrack.class);
    }

    private static <T extends Exception> Mono<T> handleClientError(ClientResponse response, T exceptionToThrow) {
        return response.bodyToMono(String.class)
                       .doOnNext(responseBody -> LOGGER.error("LastFM microservice response: {}, {}", response.statusCode(), responseBody))
                       .then(Mono.just(exceptionToThrow));
    }

}
