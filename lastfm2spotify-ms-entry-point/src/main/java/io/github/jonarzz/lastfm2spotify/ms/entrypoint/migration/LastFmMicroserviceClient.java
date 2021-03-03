package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import io.github.jonarzz.lastfm2spotify.commons.dto.LovedTrack;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

class LastFmMicroserviceClient {

    private WebClient lastFmMsClient;

    LastFmMicroserviceClient(WebClient lastFmMsClient) {
        this.lastFmMsClient = lastFmMsClient;
    }

    Flux<LovedTrack> getLovedTracks(String lastFmUsername) {
        return lastFmMsClient.get()
                             .uri("tracks/loved/{lastFmUsername}", lastFmUsername)
                             .exchangeToFlux(response -> response.bodyToFlux(LovedTrack.class));
    }

}
