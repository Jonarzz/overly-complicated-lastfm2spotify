package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class MigrationEventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MigrationEventService.class);

    private MigrationEventEmitterCreationStrategy migrationEventEmitterCreationStrategy;

    private Map<String, SseEmitter> sseEmittersGroupedByLastFmUsername;

    MigrationEventService(MigrationEventEmitterCreationStrategy migrationEventEmitterCreationStrategy) {
        this.migrationEventEmitterCreationStrategy = migrationEventEmitterCreationStrategy;
        sseEmittersGroupedByLastFmUsername = new ConcurrentHashMap<>();
    }

    SseEmitter getEmitter(String lastFmUsername) {
        return sseEmittersGroupedByLastFmUsername.computeIfAbsent(lastFmUsername, migrationEventEmitterCreationStrategy::create);
    }

    void emit(String lastFmUsername, Object emittable) {
        LOGGER.info("Emitting {} for LastFM user: {}", emittable, lastFmUsername);
        try {
            getEmitter(lastFmUsername)
                    .send(emittable);
        } catch (IOException exception) {
            LOGGER.error("Cannot emit event " + emittable + " for LastFM user '" + lastFmUsername + "', reason: " + exception.getMessage(),
                         exception);
        }
    }

}
