package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import org.springframework.beans.factory.annotation.Value;
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
    WebClient lastFmMsClient(@Value("${lastfm2spotify.integration.ms.lastfm.base-url}") String baseUrl) {
        return createWebClient(baseUrl);
    }

    @Bean
    WebClient spotifyMsClient(@Value("${lastfm2spotify.integration.ms.spotify.base-url}") String baseUrl) {
        return createWebClient(baseUrl);
    }

    private static WebClient createWebClient(String baseUrl) {
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        return WebClient.create(baseUrl);
    }

}
