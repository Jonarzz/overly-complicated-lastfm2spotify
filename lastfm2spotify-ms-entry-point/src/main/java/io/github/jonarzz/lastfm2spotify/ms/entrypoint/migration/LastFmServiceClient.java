package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jonarzz.lastfm2spotify.commons.dto.LovedTrack;
import io.github.jonarzz.lastfm2spotify.commons.error.ErrorResponse;
import io.github.jonarzz.lastfm2spotify.commons.error.ExternalApiUnavailableException;
import io.github.jonarzz.lastfm2spotify.commons.error.OtherInternalApiException;
import io.github.jonarzz.lastfm2spotify.commons.error.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

class LastFmServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(LastFmServiceClient.class);

    private WebClient lastFmMsClient;
    private ObjectMapper objectMapper;

    LastFmServiceClient(WebClient lastFmMsClient, ObjectMapper objectMapper) {
        this.lastFmMsClient = lastFmMsClient;
        this.objectMapper = objectMapper;
    }

    Flux<LovedTrack> getLovedTracks(String lastFmUsername) {
        return lastFmMsClient.get()
                             .uri("tracks/loved/{lastFmUsername}", lastFmUsername)
                             .retrieve()
                             .onStatus(HttpStatus.NO_CONTENT::equals,
                                       response -> handleError(response,
                                                               message -> new UserHasNoLovedTracksException("User " + lastFmUsername + " has no loved tracks")))
                             .onStatus(HttpStatus.NOT_FOUND::equals,
                                       response -> handleError(response,
                                                               message -> new ResourceNotFoundException("User " + lastFmUsername + " not found")))
                             .onStatus(HttpStatus.BAD_GATEWAY::equals,
                                       response -> handleError(response,
                                                               message -> new ExternalApiUnavailableException("LastFM API is not unavailable at the moment")))
                             .onStatus(HttpStatus::isError,
                                       response -> handleError(response, OtherInternalApiException::new))
                             .bodyToFlux(LovedTrack.class);
    }

    private <T extends Exception> Mono<T> handleError(ClientResponse response, Function<String, T> exceptionCreator) {
        return response.bodyToMono(String.class)
                       .defaultIfEmpty("")
                       .map(body -> {
                           try {
                               String errorMessage = objectMapper.readValue(body, ErrorResponse.class)
                                                                 .getErrorMessage();
                               LOGGER.error("LastFM microservice returned {} with message: {}",
                                            response.statusCode(), errorMessage);
                               return errorMessage;
                           } catch (JsonProcessingException e) {
                               LOGGER.error("LastFM microservice returned {} with body: {}",
                                            response.statusCode(), body);
                               return body;
                           }
                       })
                       .map(exceptionCreator);
    }

}
