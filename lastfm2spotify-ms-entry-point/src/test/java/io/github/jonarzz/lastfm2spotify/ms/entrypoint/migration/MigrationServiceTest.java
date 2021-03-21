package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import static io.github.jonarzz.lastfm2spotify.commons.test.reactive.ReactiveTestUtils.emitEvents;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import io.github.jonarzz.lastfm2spotify.commons.dto.LovedTrack;
import io.github.jonarzz.lastfm2spotify.ms.entrypoint.integration.LastFmServiceClient;
import io.github.jonarzz.lastfm2spotify.ms.entrypoint.integration.SpotifyMicroserviceClient;
import io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist.CreatedPlaylist;
import io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist.PlaylistToCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DisplayName("Migration service tests")
class MigrationServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MigrationServiceTest.class);

    @Mock
    private LastFmServiceClient lastFmClient;
    @Mock
    private SpotifyMicroserviceClient spotifyClient;
    @Mock
    private MigrationEventEmitters<String> migrationEventEmitters;
    private MigrationService migrationService = new MigrationService(lastFmClient, spotifyClient, migrationEventEmitters);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        migrationService = new MigrationService(lastFmClient, spotifyClient, migrationEventEmitters);

        doAnswer(invocation -> {
            LOGGER.info("Event emitted: " + invocation.getArgument(1));
            return null;
        }).when(migrationEventEmitters)
          .emit(any(), any());
    }

    @Test
    @DisplayName("Successful migration")
    void successfulMigration() {
        // inputs
        String lastFmUsername = "lf_user";
        String playlistName = "Playlist name";
        PlaylistToCreate playlistToCreate = PlaylistToCreate.withName(playlistName);
        // internals
        LovedTrack[] lovedTracks = new LovedTrack[] {
                new LovedTrack("Onyx", "Last Dayz"),
                new LovedTrack("Wu-Tang Clan", "Tearz"),
                new LovedTrack("GZA", "Shadowboxin'")
        };
        String playlistId = "playlist_id";
        String playlistUrl = "http://spotify.com/created-playlist-url";
        // behaviours
        when(lastFmClient.getLovedTracks(lastFmUsername))
                .thenReturn(Flux.create(sink -> {
                    emitEvents(sink, lovedTracks);
                    sink.complete();
                }));
        doAnswer(invocation -> {
            CreatedPlaylist createdPlaylist = invocation.getArgument(0);
            createdPlaylist.created(playlistId, playlistUrl);
            return null;
        }).when(spotifyClient)
          .createPlaylist(any());
        when(spotifyClient.addTrack(any(), any()))
                .thenReturn(true)
                .thenReturn(false)
                .thenReturn(true);

        Mono<CreatedPlaylist> createdPlaylist = migrationService.migrateLastFmLovedTracksToSpotifyPlaylist(lastFmUsername, playlistToCreate);

        StepVerifier.create(createdPlaylist)
                    .expectNextMatches(playlist -> playlistId.equals(playlist.getId())
                                                   && playlistUrl.equals(playlist.getUrl()))
                    .verifyComplete();
        InOrder emittersVerification = inOrder(migrationEventEmitters);
        assertAll(
                "Event emitting order",
                () -> emittersVerification.verify(migrationEventEmitters)
                                          .emit(lastFmUsername,
                                                "Migrating loved tracks for user: %s to Spotify playlist with name: %s".formatted(lastFmUsername, playlistName)),
                () -> emittersVerification.verify(migrationEventEmitters)
                                          .emit(lastFmUsername,
                                                "Creating Spotify playlist: " + playlistName),
                () -> emittersVerification.verify(migrationEventEmitters)
                                          .emit(lastFmUsername,
                                                "Adding LastFM loved tracks to Spotify playlist: " + playlistName),
                () -> emittersVerification.verify(migrationEventEmitters)
                                          .emit(lastFmUsername,
                                                "Done creating playlist '%s' with %d track(s)".formatted(playlistName, 2)),
                () -> emittersVerification.verify(migrationEventEmitters)
                                          .dispose(lastFmUsername)
        );
    }

}