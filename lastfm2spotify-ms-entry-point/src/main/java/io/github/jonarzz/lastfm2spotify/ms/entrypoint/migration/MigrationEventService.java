package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

class MigrationEventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MigrationEventService.class);

    private MigrationEventEmitters migrationEventEmitters;

    MigrationEventService(MigrationEventEmitters migrationEventEmitters) {
        this.migrationEventEmitters = migrationEventEmitters;
    }

    SseEmitter createEmitter(String lastFmUsername) {
        return migrationEventEmitters.create(lastFmUsername);
    }

    void emit(String lastFmUsername, Object emittable) {
        LOGGER.info("Emitting '{}' for LastFM user: {}", emittable, lastFmUsername);
        for (SseEmitter emitter : migrationEventEmitters.get(lastFmUsername)) {
            try {
                emitter.send(emittable);
            } catch (IOException exception) {
                LOGGER.error("Cannot for LastFM user '%s', reason: %s".formatted(lastFmUsername, exception.getMessage()));
                migrationEventEmitters.dispose(lastFmUsername, emitter);
            }
        }
    }

    void completeEmitting(String lastFmUsername) {
        migrationEventEmitters.dispose(lastFmUsername);
    }

}
