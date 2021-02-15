package io.github.jonarzz.lastfm2spotify.ms.lastfm.loved;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

class LovedTracksService {

    private static final String GET_LOVED_TRACKS_URI_TEMPLATE = "method=user.getlovedtracks&user={username}&api_key={apiKey}&format=json";

    private WebClient client;

    LovedTracksService(WebClient client) {
        this.client = client;
    }

    Flux<LovedTrack> getLovedTracks(String username) {
        return client.get()
                     .uri(GET_LOVED_TRACKS_URI_TEMPLATE, username, "TODO api key")
                     .retrieve()
                     .bodyToFlux(LovedTrack.class);
    }
}
