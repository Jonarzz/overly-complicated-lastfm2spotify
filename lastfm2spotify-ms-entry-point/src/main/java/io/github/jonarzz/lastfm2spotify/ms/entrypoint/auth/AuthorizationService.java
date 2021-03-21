package io.github.jonarzz.lastfm2spotify.ms.entrypoint.auth;

import io.github.jonarzz.lastfm2spotify.ms.entrypoint.integration.SpotifyMicroserviceClient;
import java.net.URI;

class AuthorizationService {

    private SpotifyMicroserviceClient spotifyClient;

    AuthorizationService(SpotifyMicroserviceClient spotifyClient) {
        this.spotifyClient = spotifyClient;
    }

    URI getSpotifyLoginUrl() {
        return spotifyClient.getSpotifyLoginUrl()
                            .map(URI::create)
                            .orElseThrow(); // TODO exception + handler
    }

}
