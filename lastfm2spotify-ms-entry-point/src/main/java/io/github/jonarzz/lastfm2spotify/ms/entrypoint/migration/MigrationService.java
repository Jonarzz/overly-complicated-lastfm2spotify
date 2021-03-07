package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import io.github.jonarzz.lastfm2spotify.commons.dto.LovedTrack;
import io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist.PlaylistToCreate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

class MigrationService {

    private LastFmServiceClient lastFmServiceClient;
    private MigrationEventEmitters<String> migrationEventEmitters;

    MigrationService(LastFmServiceClient lastFmServiceClient, MigrationEventEmitters<String> migrationEventEmitters) {
        this.lastFmServiceClient = lastFmServiceClient;
        this.migrationEventEmitters = migrationEventEmitters;
    }

    URI migrateLastFmLovedTracksToSpotifyPlaylist(String lastFmUsername, PlaylistToCreate playlist) {
        migrationEventEmitters.emit(lastFmUsername, "Retrieving LastFM loved tracks...");
        Collection<LovedTrack> lovedTracks = new ArrayList<>();
        lastFmServiceClient.getLovedTracks(lastFmUsername)
                           .doOnComplete(() -> migrationEventEmitters.emit(lastFmUsername, "Retrieved " + lovedTracks.size() + " loved tracks total"))
                           .subscribe(lovedTracks::add);
        // TODO handle above results properly (and test error handling)
        migrationEventEmitters.emit(lastFmUsername, "Creating Spotify playlist with name " + playlist.getName());
        // TODO call ms-spotify POST /playlist
        migrationEventEmitters.emit(lastFmUsername, "Adding LastFM loved tracks to Spotify playlist...");
        // TODO call ms-spotify GET /track?query="..."
        // TODO call ms-spotify POST /playlist/{id}/{trackId}
        // TODO emit progress events
        migrationEventEmitters.emit(lastFmUsername, "Done!");
        migrationEventEmitters.dispose(lastFmUsername);
        // TODO created playlist URI
        return URI.create("http://www.google.com/search?q=spotify+playlist");
    }

}
