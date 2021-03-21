package io.github.jonarzz.lastfm2spotify.ms.entrypoint.auth;

import io.github.jonarzz.lastfm2spotify.ms.entrypoint.integration.SpotifyMicroserviceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AuthorizationConfiguration {

    @Bean
    AuthorizationService authorizationService(SpotifyMicroserviceClient spotifyClient) {
        return new AuthorizationService(spotifyClient);
    }

}
