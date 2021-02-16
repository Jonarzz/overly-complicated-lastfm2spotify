package io.github.jonarzz.lastfm2spotify.ms.lastfm.loved;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class LovedTracksService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LovedTracksService.class);

    private static final String GET_LOVED_TRACKS_URI_TEMPLATE = "method=user.getlovedtracks&user={username}&api_key={apiKey}&page={page}&format=json";

    private WebClient client;
    private String apiKey;

    LovedTracksService(WebClient client, String apiKey) {
        this.client = client;
        this.apiKey = apiKey;
    }

    Flux<LovedTrack> getLovedTracks(String username) {
        return getLovedTracksPage(username, 1)
                .expand(response -> {
                    PagingMetadata pagingMetadata = response.getPagingMetadata();
                    int currentPage = pagingMetadata.getPage();
                    if (currentPage >= pagingMetadata.getTotalPages()) {
                        return Mono.empty();
                    }
                    return getLovedTracksPage(username, currentPage + 1);
                })
                .map(LovedTracksApiResponse::getLovedTracks)
                .flatMapSequential(Flux::fromIterable);
    }

    private Mono<LovedTracksApiResponse> getLovedTracksPage(String username, int pageNumber) {
        LOGGER.info("Retrieving loved tracks for user {}, page {}", username, pageNumber);
        return client.get()
                     .uri(GET_LOVED_TRACKS_URI_TEMPLATE, username, apiKey, pageNumber)
                     .retrieve()
                     .bodyToMono(LovedTracksApiResponse.class);
    }

}
