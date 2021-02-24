package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist.PlaylistToCreate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("migration")
@CrossOrigin("${lastfm2spotify.web.accepted-origin-host}")
public class MigrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MigrationController.class);

    private MigrationService migrationService;
    private MigrationEventEmitters<String> migrationEventEmitters;

    MigrationController(MigrationService migrationService, MigrationEventEmitters<String> migrationEventEmitters) {
        this.migrationService = migrationService;
        this.migrationEventEmitters = migrationEventEmitters;
    }

    @GetMapping(value = "{lastFmUsername}/loved/status", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> subscribeForMigrationEvents(@PathVariable String lastFmUsername) {
        LOGGER.info("Retrieved a subscription for LastFM loved migration events for user: {}", lastFmUsername);
        return migrationEventEmitters.provide(lastFmUsername);
    }

    @PostMapping("{lastFmUsername}/loved")
    public URI migrateLastFmLovedTracksToSpotifyPlaylist(@PathVariable String lastFmUsername,
                                                         @RequestBody @Valid PlaylistToCreate playlist) {
        LOGGER.info("Starting LastFM user '{}' loved tracks migration to Spotify playlist: {}", lastFmUsername, playlist);
        return migrationService.migrateLastFmLovedTracksToSpotifyPlaylist(lastFmUsername, playlist);
    }

}
