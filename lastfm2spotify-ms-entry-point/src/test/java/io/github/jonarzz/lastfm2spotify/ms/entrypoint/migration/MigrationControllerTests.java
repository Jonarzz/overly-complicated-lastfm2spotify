package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import static io.github.jonarzz.lastfm2spotify.commons.test.reactive.ReactiveTestUtils.emitEvents;
import static io.github.jonarzz.lastfm2spotify.commons.test.web.RestDocsConfiguration.documentWithPrettyPrint;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist.CreatedPlaylist;
import io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist.PlaylistToCreate;
import io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist.PrivacyConfig;
import io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist.SongsOrdering;
import io.github.jonarzz.lastfm2spotify.ms.entrypoint.testutil.DocumentedControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@DocumentedControllerTest
@DisplayName("Migration controller tests")
@WebFluxTest(MigrationController.class)
@Execution(ExecutionMode.CONCURRENT)
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
              .consumeWith(documentWithPrettyPrint("lovedMigrationStatus"))
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
                .thenReturn(Mono.just(CreatedPlaylist.beforeCreation(playlistToCreate)
                                                     .created("playlist id", expectedPlaylistUrl)));

        client.post()
              .uri("/migration/{lastFmUsername}/loved", lastFmUsername)
              .body(fromValue("{\"name\": \"%s\"}".formatted(playlistName)))
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .exchange()
              .expectStatus().isOk()
              .expectHeader().contentType(MediaType.APPLICATION_JSON)
              .expectBody()
              .consumeWith(documentWithPrettyPrint("playlistWithOnlyRequiredFields"))
              .jsonPath("$", expectedPlaylistUrl);
    }

    @Test
    @DisplayName("Playlist is created - all playlist params are filled in")
    void playlistIsCreated_allPlaylistParamsAreFilledIn() {
        String lastFmUsername = "test_user";
        PlaylistToCreate playlistToCreate = PlaylistToCreate.withConfiguration("My best playlist", PrivacyConfig.PRIVATE, SongsOrdering.NEW_LAST);
        String expectedPlaylistUrl = "https://accounts.spotify.com/pl/authorize?client_id=123";
        when(migrationService.migrateLastFmLovedTracksToSpotifyPlaylist(lastFmUsername, playlistToCreate))
                .thenReturn(Mono.just(CreatedPlaylist.beforeCreation(playlistToCreate)
                                                     .created("playlist id", expectedPlaylistUrl)));

        client.post()
              .uri("/migration/{lastFmUsername}/loved", lastFmUsername)
              .body(Mono.just(playlistToCreate), PlaylistToCreate.class)
              .exchange()
              .expectStatus().isOk()
              .expectHeader().contentType(MediaType.APPLICATION_JSON)
              .expectBody()
              .consumeWith(documentWithPrettyPrint("playlistWithAllFields"))
              .jsonPath("$", expectedPlaylistUrl);
    }

}