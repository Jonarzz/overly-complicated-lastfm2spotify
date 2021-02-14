package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class MigrationConfiguration {

    @Bean
    MigrationEventService migrationEventService(MigrationEventEmitters migrationEventEmitters) {
        return new MigrationEventService(migrationEventEmitters);
    }

    @Bean
    MigrationEventEmitters migrationEventEmitterCreationStrategy() {
        return new DefaultMigrationEventEmitters();
    }

    @Bean
    MigrationService migrationService(MigrationEventService migrationEventService) {
        return new MigrationService(migrationEventService);
    }

}
