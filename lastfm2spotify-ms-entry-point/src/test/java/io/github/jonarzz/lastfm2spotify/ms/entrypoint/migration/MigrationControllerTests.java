package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist.PlaylistToCreate;
import io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist.PrivacyConfig;
import io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist.SongsOrdering;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@DisplayName("Migration events tests")
@WebFluxTest(MigrationController.class)
@TestPropertySource(properties = "lastfm2spotify.web.accepted-origin-host=localhost")
@AutoConfigureRestDocs
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
class MigrationControllerTests {

    @MockBean
    private MigrationEventEmitters<String> migrationEventEmitters;
    @MockBean
    private MigrationService migrationService;
    @Autowired
    private WebTestClient client;

    @Test
    @DisplayName("Loved tracks migration status events are emitted")
    void lovedTracksMigrationStatusEventsAreEmitted() {
        String lastFmUsername = "test_user";
        String[] events = {
                "Fetching LastFM loved tracks...",
                "Done fetching LastFM loved tracks",
                "Created Spotify playlist",
                "Adding tracks to the playlist...",
                "Done adding tracks to the playlist"
        };
        when(migrationEventEmitters.provide(lastFmUsername))
                .thenReturn(Flux.create(sink -> {
                    emitEvents(sink, events);
                    sink.complete();
                }));

        client.get()
              .uri("/migration/{lastFmUsername}/loved/status", lastFmUsername)
              .exchange()
              .expectStatus().isOk()
              .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
              .expectBodyList(String.class)
              .consumeWith(document("lovedMigrationStatus",
                                    preprocessRequest(prettyPrint(),
                                                      removeHeaders("Host")),
                                    preprocessResponse(prettyPrint(),
                                                       removeHeaders("Vary", "Content-Length"))))
              .hasSize(events.length)
              .contains(events);
    }

    @Test
    @DisplayName("Playlist is created - only required playlist params are filled in")
    void playlistIsCreated_onlyRequiredPlaylistParamsAreFilledIn() {
        String lastFmUsername = "test_user";
        String playlistName = "My simple playlist";
        PlaylistToCreate playlistToCreate = PlaylistToCreate.withName(playlistName);
        String expectedPlaylistUrl = "https://accounts.spotify.com/pl/authorize?client_id=987";
        when(migrationService.migrateLastFmLovedTracksToSpotifyPlaylist(lastFmUsername, playlistToCreate))
                .thenReturn(URI.create(expectedPlaylistUrl));

        client.post()
              .uri("/migration/{lastFmUsername}/loved", lastFmUsername)
              .body(fromValue("{\"name\": \"%s\"}" .formatted(playlistName)))
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .exchange()
              .expectStatus().isOk()
              .expectHeader().contentType(MediaType.APPLICATION_JSON)
              .expectBody()
              .consumeWith(document("playlistWithOnlyRequiredFields",
                                    preprocessRequest(prettyPrint(),
                                                      removeHeaders("Host")),
                                    preprocessResponse(prettyPrint(),
                                                       removeHeaders("Vary", "Content-Length"))))
              .jsonPath("$", expectedPlaylistUrl);
    }

    @Test
    @DisplayName("Playlist is created - all playlist params are filled in")
    void playlistIsCreated_allPlaylistParamsAreFilledIn() {
        String lastFmUsername = "test_user";
        PlaylistToCreate playlistToCreate = PlaylistToCreate.withConfiguration("My best playlist", PrivacyConfig.PRIVATE, SongsOrdering.NEW_LAST);
        String expectedPlaylistUrl = "https://accounts.spotify.com/pl/authorize?client_id=123";
        when(migrationService.migrateLastFmLovedTracksToSpotifyPlaylist(lastFmUsername, playlistToCreate))
                .thenReturn(URI.create(expectedPlaylistUrl));

        client.post()
              .uri("/migration/{lastFmUsername}/loved", lastFmUsername)
              .body(Mono.just(playlistToCreate), PlaylistToCreate.class)
              .exchange()
              .expectStatus().isOk()
              .expectHeader().contentType(MediaType.APPLICATION_JSON)
              .expectBody()
              .consumeWith(document("playlistWithAllFields",
                                    preprocessRequest(prettyPrint(),
                                                      removeHeaders("Host")),
                                    preprocessResponse(prettyPrint(),
                                                       removeHeaders("Vary", "Content-Length"))))
              .jsonPath("$", expectedPlaylistUrl);
    }

    @SafeVarargs
    private <T> void emitEvents(FluxSink<T> sink, T... events) {
        Stream.of(events)
              .forEach(event -> {
                  try {
                      TimeUnit.MILLISECONDS.sleep(100);
                  } catch (InterruptedException e) {
                      Thread.currentThread().interrupt();
                  }
                  sink.next(event);
              });
    }

}