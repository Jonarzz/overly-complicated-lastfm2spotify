package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

class MulticastStringEventEmitters implements MigrationEventEmitters<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MulticastStringEventEmitters.class);

    private Supplier<Sinks.Many<String>> sinkSupplier;

    private Map<String, Sinks.Many<String>> sinkForUsername = new ConcurrentHashMap<>();

    MulticastStringEventEmitters(Supplier<Sinks.Many<String>> sinkSupplier) {
        this.sinkSupplier = sinkSupplier;
    }

    @Override
    public Flux<String> provide(String lastFmUsername) {
        return Optional.ofNullable(sinkForUsername.get(lastFmUsername))
                       .map(Sinks.Many::asFlux)
                       .orElseGet(() -> {
                           String clientSentMessage = "No active event emitter for user " + lastFmUsername;
                           LOGGER.info("{} <- emitting infinite flux with just this info", clientSentMessage);
                           return Flux.merge(Flux.just(clientSentMessage),
                                             Flux.never());
                       });
    }

    @Override
    public void emit(String lastFmUsername, String event) {
        LOGGER.info("Emitting event '{}' for user {}", event, lastFmUsername);
        var sink = sinkForUsername.computeIfAbsent(lastFmUsername,
                                                   username -> sinkSupplier.get());
        var emitResult = sink.tryEmitNext(event);
        if (emitResult.isFailure()) {
            LOGGER.warn("Emitting '{}' event for user {} ended up in failure: {}",
                        event, lastFmUsername, emitResult);
        }
    }

    @Override
    public void dispose(String lastFmUsername) {
        Optional.ofNullable(sinkForUsername.get(lastFmUsername))
                .ifPresent(sink -> {
                    LOGGER.info("Disposing emitter for user {}", lastFmUsername);
                    Sinks.EmitResult emitResult = sink.tryEmitComplete();
                    sinkForUsername.remove(lastFmUsername);
                    if (emitResult.isFailure()) {
                        LOGGER.info("Emitting complete event for user {} ended up in failure: {}",
                                    lastFmUsername, emitResult);
                    }
                });
    }

}
