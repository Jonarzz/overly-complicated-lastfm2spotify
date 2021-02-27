package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import reactor.core.publisher.Flux;

interface MigrationEventEmitters<T> {

    Flux<T> provide(String lastFmUsername);

    void emit(String lastFmUsername, T event);

    void dispose(String lastFmUsername);

}
