package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

class DefaultMigrationEventEmitterCreationStrategy implements MigrationEventEmitterCreationStrategy {

    @Override
    public SseEmitter create(String lastFmUsername) {
        return new SseEmitter();
    }

}
