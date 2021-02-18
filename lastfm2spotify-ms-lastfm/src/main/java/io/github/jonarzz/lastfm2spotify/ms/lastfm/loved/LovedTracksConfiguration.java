package io.github.jonarzz.lastfm2spotify.ms.lastfm.loved;

import io.github.jonarzz.lastfm2spotify.ms.lastfm.LastFmApiProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
class LovedTracksConfiguration {

    private LastFmApiProperties properties;

    LovedTracksConfiguration(LastFmApiProperties properties) {
        this.properties = properties;
    }

    @Bean
    LovedTracksService lovedTracksService(WebClient lastFmApiClient) {
        return new LovedTracksService(lastFmApiClient, properties.apiKey(), properties.singlePageLimit());
    }

    @Bean
    WebClient lastFmApiClient() {
        return WebClient.builder()
                        .baseUrl(properties.baseUrl())
                        .build();
    }

}
