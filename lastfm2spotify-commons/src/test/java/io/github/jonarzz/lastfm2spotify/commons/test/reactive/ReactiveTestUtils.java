package io.github.jonarzz.lastfm2spotify.commons.test.reactive;

import reactor.core.publisher.FluxSink;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class ReactiveTestUtils {

    private ReactiveTestUtils() {
    }

    @SafeVarargs
    public static <T> void emitEvents(FluxSink<T> sink, T... events) {
        Stream.of(events)
              .forEach(event -> {
                  try {
                      TimeUnit.MILLISECONDS.sleep(50);
                  } catch (InterruptedException e) {
                      Thread.currentThread().interrupt();
                  }
                  sink.next(event);
              });
    }

}
