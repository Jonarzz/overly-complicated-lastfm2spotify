package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import io.github.jonarzz.lastfm2spotify.ms.entrypoint.integration.LastFmServiceClient;
import io.github.jonarzz.lastfm2spotify.ms.entrypoint.integration.SpotifyMicroserviceClient;
import io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist.CreatedPlaylist;
import io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist.PlaylistToCreate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

class MigrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MigrationService.class);

    private LastFmServiceClient lastFmServiceClient;
    private SpotifyMicroserviceClient spotifyMicroserviceClient;
    private MigrationEventEmitters<String> migrationEventEmitters;

    MigrationService(LastFmServiceClient lastFmServiceClient,
                     SpotifyMicroserviceClient spotifyMicroserviceClient,
                     MigrationEventEmitters<String> migrationEventEmitters) {
        this.lastFmServiceClient = lastFmServiceClient;
        this.spotifyMicroserviceClient = spotifyMicroserviceClient;
        this.migrationEventEmitters = migrationEventEmitters;
    }

    Mono<CreatedPlaylist> migrateLastFmLovedTracksToSpotifyPlaylist(String lastFmUsername, PlaylistToCreate playlistToCreate) {
        String playlistName = playlistToCreate.getName();
        migrationEventEmitters.emit(lastFmUsername,
                                    "Migrating loved tracks for user: %s to Spotify playlist with name: %s".formatted(lastFmUsername, playlistName));
        CreatedPlaylist createdPlaylist = CreatedPlaylist.beforeCreation(playlistToCreate);
        // TODO error handling
        return lastFmServiceClient.getLovedTracks(lastFmUsername)
                                  .switchOnFirst((trackSignal, tracksFlux) -> {
                                           if (trackSignal.hasValue()) {
                                               migrationEventEmitters.emit(lastFmUsername, "Creating Spotify playlist: " + playlistName);
                                               spotifyMicroserviceClient.createPlaylist(createdPlaylist);
                                               migrationEventEmitters.emit(lastFmUsername, "Adding LastFM loved tracks to Spotify playlist: " + playlistName);
                                           }
                                           return tracksFlux;
                                       })
                                  .map(lovedTrack -> {
                                           boolean trackAdded = spotifyMicroserviceClient.addTrack(createdPlaylist, lovedTrack);
                                           LOGGER.debug("{} {}added to playlist: {}", lovedTrack, trackAdded ? "" : "not ", playlistName);
                                           return trackAdded ? 1 : 0;
                                       })
                                  .reduce(0, Integer::sum)
                                  .map(totalTracksAdded -> {
                                           migrationEventEmitters.emit(lastFmUsername,
                                                                       "Done creating playlist '%s' with %d track(s)".formatted(playlistName, totalTracksAdded));
                                           migrationEventEmitters.dispose(lastFmUsername);
                                           return createdPlaylist;
                                       });
    }

}
