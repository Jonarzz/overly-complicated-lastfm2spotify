package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@DisplayName("Multicast string event emitters tests")
class MulticastStringEventEmittersTest {

    private MigrationConfiguration configuration = new MigrationConfiguration();

    private MigrationEventEmitters<String> emitters;

    @BeforeEach
    void setUp() {
        emitters = configuration.migrationEventEmitters(configuration.multicastStringSink());
    }

    @Test
    @DisplayName("Try to subscribe - no active emitter for given user")
    void tryToSubscribe_noActiveEmitterForGivenUser() {
        String username = "test_user";

        Flux<String> emitter = emitters.provide(username);

        StepVerifier.create(emitter)
                    .expectNext("No active event emitter for user " + username)
                    .verifyTimeout(Duration.ofMillis(100));
    }

    @Test
    @DisplayName("Subscribe - emitting already started - no new events")
    void subscribe_emittingAlreadyStarted_noNewEvents() {
        String username = "test_user";
        String firstEvent = "first event";

        emitters.emit(username, firstEvent);
        Flux<String> emitter = emitters.provide(username);

        StepVerifier.create(emitter)
                    .expectNext(firstEvent)
                    .verifyTimeout(Duration.ofMillis(100));
    }

    @Test
    @DisplayName("Subscribe - emitting already started - new events emitted after subscription")
    void subscribe_emittingAlreadyStarted_newEventsEmittedAfterSubscription() {
        String username = "test_user";
        String firstEvent = "first event";
        String secondEvent = "secondEvent event";

        emitters.emit(username, firstEvent);
        Flux<String> emitter = emitters.provide(username);
        emitters.emit(username, secondEvent);

        StepVerifier.create(emitter)
                    .expectNext(firstEvent)
                    .expectNext(secondEvent)
                    .verifyTimeout(Duration.ofMillis(100));
    }

    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Subscribe - emitting already started - emitter disposed after subscription")
    void subscribe_emittingAlreadyStarted_emitterDisposedAfterSubscription() {
        String username = "test_user";
        String firstEvent = "first event";

        emitters.emit(username, firstEvent);
        Flux<String> emitter = emitters.provide(username);
        emitters.dispose(username);

        StepVerifier.create(emitter)
                    .expectNext(firstEvent)
                    .verifyComplete();
    }

    @Test
    @DisplayName("Try to subscribe - emitting already done")
    void tryToSubscribe_emittingAlreadyDone() {
        String username = "test_user";

        emitters.emit(username, "first event");
        emitters.dispose(username);
        Flux<String> emitter = emitters.provide(username);

        StepVerifier.create(emitter)
                    .expectNext("No active event emitter for user " + username)
                    .verifyTimeout(Duration.ofMillis(100));
    }

    @Test
    @DisplayName("Try to emit - failure")
    @SuppressWarnings("unchecked")
    void tryToEmit_failure() {
        Sinks.Many<String> mockedSink = (Sinks.Many<String>) mock(Sinks.Many.class);
        MigrationEventEmitters<String> emittersWithMockedSink = configuration.migrationEventEmitters(() -> mockedSink);
        String event = "event";
        when(mockedSink.tryEmitNext(event))
                .thenReturn(Sinks.EmitResult.FAIL_NON_SERIALIZED);

        emittersWithMockedSink.emit("test_user", event);

        verify(mockedSink)
                .tryEmitNext(event);
    }

    @Test
    @DisplayName("Try to emit - failure")
    @SuppressWarnings("unchecked")
    void tryToDispose_failure() {
        Sinks.Many<String> mockedSink = (Sinks.Many<String>) mock(Sinks.Many.class);
        MigrationEventEmitters<String> emittersWithMockedSink = configuration.migrationEventEmitters(() -> mockedSink);
        String username = "test_user";
        String event = "event";
        when(mockedSink.tryEmitNext(event))
                .thenReturn(Sinks.EmitResult.OK);
        when(mockedSink.tryEmitComplete())
                .thenReturn(Sinks.EmitResult.FAIL_OVERFLOW);

        emittersWithMockedSink.emit(username, event);
        emittersWithMockedSink.dispose(username);

        verify(mockedSink)
                .tryEmitComplete();
    }

}