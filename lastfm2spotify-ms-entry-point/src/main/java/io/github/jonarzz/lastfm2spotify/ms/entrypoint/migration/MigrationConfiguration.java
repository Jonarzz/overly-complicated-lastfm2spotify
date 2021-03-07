package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import io.github.jonarzz.lastfm2spotify.ms.entrypoint.MicroserviceIntegrationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
class MigrationConfiguration {

    @Bean
    Supplier<Sinks.Many<String>> multicastStringSink() {
        return () -> Sinks.many()
                          .multicast()
                          .onBackpressureBuffer();
    }

    @Bean
    MigrationEventEmitters<String> migrationEventEmitters(Supplier<Sinks.Many<String>> multicastStringSink) {
        return new MulticastStringEventEmitters(multicastStringSink);
    }

    @Bean
    MigrationService migrationService(MigrationEventEmitters<String> migrationEventEmitters,
                                      LastFmMicroserviceClient lastFmMicroserviceClient) {
        return new MigrationService(lastFmMicroserviceClient, migrationEventEmitters);
    }

    @Bean
    LastFmMicroserviceClient lastFmMicroserviceClient(WebClient lastFmMsClient) {
        return new LastFmMicroserviceClient(lastFmMsClient);
    }

    @Bean
    WebClient lastFmMsClient(MicroserviceIntegrationProperties integrationProperties) {
        return createWebClient(integrationProperties.getLastFm()
                                                    .getBaseUrl());
    }

    @Bean
    WebClient spotifyMsClient(MicroserviceIntegrationProperties integrationProperties) {
        return createWebClient(integrationProperties.getSpotify()
                                                    .getBaseUrl());
    }

    private static WebClient createWebClient(String baseUrl) {
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        return WebClient.create(baseUrl);
    }

}
