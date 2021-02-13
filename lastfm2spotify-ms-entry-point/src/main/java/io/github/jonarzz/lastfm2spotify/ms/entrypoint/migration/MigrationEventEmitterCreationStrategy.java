package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

interface MigrationEventEmitterCreationStrategy {

    SseEmitter create(String lastFmUsername);

}
