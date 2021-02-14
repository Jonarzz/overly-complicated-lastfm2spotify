package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist.PlaylistToCreate;

import java.net.URI;

class MigrationService {

    private MigrationEventService migrationEventService;

    MigrationService(MigrationEventService migrationEventService) {
        this.migrationEventService = migrationEventService;
    }

    URI migrateLastFmLovedTracksToSpotifyPlaylist(String lastFmUsername, PlaylistToCreate playlist) {
        migrationEventService.emit(lastFmUsername, "Retrieving LastFM loved tracks...");
        // TODO call ms-lastfm GET /user/{lastFmUsername}/loved
        migrationEventService.emit(lastFmUsername, "Creating Spotify playlist...");
        // TODO call ms-spotify POST /playlist
        migrationEventService.emit(lastFmUsername, "Adding LastFM loved tracks to Spotify playlist...");
        // TODO call ms-spotify GET /track?query="..."
        // TODO call ms-spotify POST /playlist/{id}/{trackId}
        // TODO emit progress events
        migrationEventService.emit(lastFmUsername, "Done!");
        migrationEventService.completeEmitting(lastFmUsername);
        // TODO created playlist URI
        return URI.create("http://www.google.com/search?q=spotify+playlist");
    }

}
