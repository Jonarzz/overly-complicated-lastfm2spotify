package io.github.jonarzz.lastfm2spotify.ms.entrypoint.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jonarzz.lastfm2spotify.ms.entrypoint.integration.IntegrationProperties.MicroserviceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class IntegrationConfiguration {

    private MicroserviceConfiguration lastFmProperties;
    private MicroserviceConfiguration spotifyProperties;

    public IntegrationConfiguration(IntegrationProperties integrationProperties) {
        lastFmProperties = integrationProperties.getLastFm();
        spotifyProperties = integrationProperties.getSpotify();
    }

    @Bean
    public LastFmServiceClient lastFmMicroserviceClient(ObjectMapper objectMapper) {
        return new LastFmServiceClient(createWebClient(lastFmProperties), objectMapper);
    }

    @Bean
    public SpotifyMicroserviceClient spotifyMicroserviceClient() {
        return new SpotifyMicroserviceClient(createWebClient(spotifyProperties));
    }

    private static WebClient createWebClient(MicroserviceConfiguration configuration) {
        return WebClient.create(configuration.getBaseUrl());
    }

}
