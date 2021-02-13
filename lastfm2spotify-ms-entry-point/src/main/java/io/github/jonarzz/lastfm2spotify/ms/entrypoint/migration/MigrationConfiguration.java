package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class MigrationConfiguration {

    @Bean
    MigrationEventService migrationEventService(MigrationEventEmitterCreationStrategy migrationEventEmitterCreationStrategy) {
        return new MigrationEventService(migrationEventEmitterCreationStrategy);
    }

    @Bean
    MigrationEventEmitterCreationStrategy migrationEventEmitterCreationStrategy() {
        return new DefaultMigrationEventEmitterCreationStrategy();
    }

    @Bean
    MigrationService migrationService(MigrationEventService migrationEventService) {
        return new MigrationService(migrationEventService);
    }

}
