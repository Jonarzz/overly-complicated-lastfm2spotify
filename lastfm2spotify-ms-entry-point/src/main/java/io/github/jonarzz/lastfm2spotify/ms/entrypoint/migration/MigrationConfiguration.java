package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class MigrationConfiguration {

    @Bean
    MigrationEventEmitters<String> migrationEventEmitters() {
        return new MulticastStringEventEmitters();
    }

    @Bean
    MigrationService migrationService(MigrationEventEmitters<String> migrationEventEmitters) {
        return new MigrationService(migrationEventEmitters);
    }

}
