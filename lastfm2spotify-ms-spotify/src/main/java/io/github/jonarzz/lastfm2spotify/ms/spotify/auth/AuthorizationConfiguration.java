package io.github.jonarzz.lastfm2spotify.ms.spotify.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AuthorizationConfiguration {

    @Bean
    AuthorizationService authorizationService(AuthorizationProperties properties) {
        return new AuthorizationService(properties.getAccess()
                                                  .getBaseUrl(),
                                        properties.getClientId());
    }

}
