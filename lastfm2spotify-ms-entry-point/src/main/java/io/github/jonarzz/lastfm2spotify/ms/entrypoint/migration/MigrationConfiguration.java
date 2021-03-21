package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import io.github.jonarzz.lastfm2spotify.ms.entrypoint.integration.LastFmServiceClient;
import io.github.jonarzz.lastfm2spotify.ms.entrypoint.integration.SpotifyMicroserviceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    MigrationService migrationService(LastFmServiceClient lastFmServiceClient,
                                      SpotifyMicroserviceClient spotifyMicroserviceClient,
                                      MigrationEventEmitters<String> migrationEventEmitters) {
        return new MigrationService(lastFmServiceClient, spotifyMicroserviceClient, migrationEventEmitters);
    }

}
