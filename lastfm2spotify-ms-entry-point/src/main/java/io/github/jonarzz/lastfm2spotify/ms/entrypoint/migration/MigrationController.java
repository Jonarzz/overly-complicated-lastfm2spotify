package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist.PlaylistToCreate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("migration")
public class MigrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MigrationController.class);

    @PostMapping("{lastFmUsername}/loved")
    public void migrateLovedTracks(@PathVariable String lastFmUsername,
                            @RequestBody @Valid PlaylistToCreate playlist) {
        LOGGER.info("Migrating LastFM user '{}' loved tracks to Spotify playlist: {}", lastFmUsername, playlist);
    }

}
