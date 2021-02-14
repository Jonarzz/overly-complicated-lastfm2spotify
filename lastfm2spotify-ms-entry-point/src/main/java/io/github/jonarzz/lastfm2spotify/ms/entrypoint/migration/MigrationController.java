package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist.PlaylistToCreate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("migration")
@CrossOrigin("${lastfm2spotify.web.accepted-origin-host}")
public class MigrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MigrationController.class);

    private MigrationService migrationService;
    private MigrationEventService migrationEventService;

    MigrationController(MigrationService migrationService, MigrationEventService migrationEventService) {
        this.migrationService = migrationService;
        this.migrationEventService = migrationEventService;
    }

    @GetMapping("{lastFmUsername}/loved/status")
    public SseEmitter subscribeForMigrationEvents(@PathVariable String lastFmUsername) {
        LOGGER.info("Retrieved a subscription for LastFM loved migration events for user: {}", lastFmUsername);
        return migrationEventService.createEmitter(lastFmUsername);
    }

    @PostMapping("{lastFmUsername}/loved")
    public URI migrateLastFmLovedTracksToSpotifyPlaylist(@PathVariable String lastFmUsername,
                                                         @RequestBody @Valid PlaylistToCreate playlist) {
        LOGGER.info("Starting LastFM user '{}' loved tracks migration to Spotify playlist: {}", lastFmUsername, playlist);
        return migrationService.migrateLastFmLovedTracksToSpotifyPlaylist(lastFmUsername, playlist);
    }

}
