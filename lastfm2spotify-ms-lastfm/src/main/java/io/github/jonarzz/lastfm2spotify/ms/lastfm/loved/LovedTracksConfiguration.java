package io.github.jonarzz.lastfm2spotify.ms.lastfm.loved;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
class LovedTracksConfiguration {

    @Bean
    LovedTracksService lovedTracksService(@Value("${lastfm2spotify.api.lastfm.base-url}") String baseUrl) {
        return new LovedTracksService(WebClient.builder()
                                               .baseUrl(baseUrl)
                                               .build());
    }

}
