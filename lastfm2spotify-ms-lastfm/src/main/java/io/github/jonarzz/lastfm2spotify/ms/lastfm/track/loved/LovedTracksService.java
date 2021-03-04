package io.github.jonarzz.lastfm2spotify.ms.lastfm.track.loved;

import io.github.jonarzz.lastfm2spotify.commons.dto.LovedTrack;
import io.github.jonarzz.lastfm2spotify.ms.lastfm.error.ExternalApiUnavailableException;
import io.github.jonarzz.lastfm2spotify.ms.lastfm.error.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class LovedTracksService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LovedTracksService.class);

    private static final String GET_LOVED_TRACKS_URI_TEMPLATE = "?method=user.getlovedtracks"
                                                                + "&user={username}"
                                                                + "&api_key={apiKey}"
                                                                + "&page={page}"
                                                                + "&limit={limit}"
                                                                + "&format=json";

    private WebClient client;
    private String apiKey;
    private int singlePageLimit;

    LovedTracksService(WebClient client, String apiKey, int singlePageLimit) {
        this.client = client;
        this.apiKey = apiKey;
        this.singlePageLimit = singlePageLimit;
    }

    Flux<LovedTrack> getLovedTracks(String username) {
        return getLovedTracksPage(username, 1)
                .expand(response -> { // TODO strategia do wyboru sposobu pobierania (rosnąco, malejąco, "losowo" - równolegle)
                    PagingMetadata pagingMetadata = response.getPagingMetadata();
                    int currentPage = pagingMetadata.getPage();
                    if (currentPage >= pagingMetadata.getTotalPages()) {
                        return Mono.empty();
                    }
                    return getLovedTracksPage(username, currentPage + 1);
                })
                .map(LastFmLovedTracksResponse::getLovedTracks)
                .flatMapSequential(Flux::fromIterable)
                .map(LastFmLovedTrack::toInternalModel);
    }

    private Mono<LastFmLovedTracksResponse> getLovedTracksPage(String username, int pageNumber) {
        LOGGER.info("Retrieving loved tracks for user {}, page {}", username, pageNumber);
        return client.get()
                     .uri(GET_LOVED_TRACKS_URI_TEMPLATE, username, apiKey, pageNumber, singlePageLimit)
                     .retrieve()
                     .onStatus(HttpStatus::is4xxClientError,
                               response -> Mono.just(new ResourceNotFoundException("User with name " + username + " not found")))
                     .onStatus(HttpStatus::is5xxServerError,
                               response -> Mono.just(new ExternalApiUnavailableException("LastFM API is unavailable at the moment")))
                     .bodyToMono(LastFmLovedTracksResponse.class)
                     .doOnNext(response -> logRequestDone(username, response));
    }

    private static void logRequestDone(String username, LastFmLovedTracksResponse response) {
        PagingMetadata pagingMetadata = response.getPagingMetadata();
        int currentPage = pagingMetadata.getPage();
        int totalTracks = pagingMetadata.getTotal();
        int numberOfTracks = response.getLovedTracks().size();
        LOGGER.info("Done retrieving loved tracks for user {}, page {} - got next {} tracks ({} total)",
                    username, currentPage, numberOfTracks, totalTracks);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Parsed response retrieved for user {}: {}", username, response);
        }
    }

}
