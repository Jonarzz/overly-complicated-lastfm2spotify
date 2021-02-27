package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist.PlaylistToCreate;

import java.net.URI;
import java.util.concurrent.TimeUnit;

class MigrationService {

    private MigrationEventEmitters<String> migrationEventEmitters;

    MigrationService(MigrationEventEmitters<String> migrationEventEmitters) {
        this.migrationEventEmitters = migrationEventEmitters;
    }

    URI migrateLastFmLovedTracksToSpotifyPlaylist(String lastFmUsername, PlaylistToCreate playlist) {
        try {
            migrationEventEmitters.emit(lastFmUsername, "Retrieving LastFM loved tracks...");
            TimeUnit.SECONDS.sleep(5);
            // TODO call ms-lastfm GET /user/{lastFmUsername}/loved
            migrationEventEmitters.emit(lastFmUsername, "Creating Spotify playlist...");
            TimeUnit.SECONDS.sleep(5);
            // TODO call ms-spotify POST /playlist
            migrationEventEmitters.emit(lastFmUsername, "Adding LastFM loved tracks to Spotify playlist...");
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // TODO call ms-spotify GET /track?query="..."
        // TODO call ms-spotify POST /playlist/{id}/{trackId}
        // TODO emit progress events
        migrationEventEmitters.emit(lastFmUsername, "Done!");
        migrationEventEmitters.dispose(lastFmUsername);
        // TODO created playlist URI
        return URI.create("http://www.google.com/search?q=spotify+playlist");
    }

}
