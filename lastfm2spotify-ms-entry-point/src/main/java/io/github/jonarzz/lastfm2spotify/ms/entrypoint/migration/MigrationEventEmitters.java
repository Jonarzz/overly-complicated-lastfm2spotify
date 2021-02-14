package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collection;

interface MigrationEventEmitters {

    SseEmitter create(String lastFmUsername);

    Collection<SseEmitter> get(String lastFmUsername);

    void dispose(String lastFmUsername);

    void dispose(String lastFmUsername, SseEmitter emitter);

}
