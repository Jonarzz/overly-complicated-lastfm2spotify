package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collection;

// TODO replace with Flux, remove spring-boot-starter-web dep. and move spring-boot-starter-webflux to parent
class DefaultMigrationEventEmitters implements MigrationEventEmitters {

    private Multimap<String, SseEmitter> sseEmittersGroupedByLastFmUsername = Multimaps.synchronizedListMultimap(ArrayListMultimap.create());

    @Override
    public SseEmitter create(String lastFmUsername) {
        SseEmitter emitter = new SseEmitter();
        sseEmittersGroupedByLastFmUsername.put(lastFmUsername, emitter);
        return emitter;
    }

    @Override
    public Collection<SseEmitter> get(String lastFmUsername) {
        return sseEmittersGroupedByLastFmUsername.get(lastFmUsername);
    }

    @Override
    public void dispose(String lastFmUsername) {
        sseEmittersGroupedByLastFmUsername.removeAll(lastFmUsername);
    }

    @Override
    public void dispose(String lastFmUsername, SseEmitter emitter) {
        sseEmittersGroupedByLastFmUsername.remove(lastFmUsername, emitter);
    }

}
