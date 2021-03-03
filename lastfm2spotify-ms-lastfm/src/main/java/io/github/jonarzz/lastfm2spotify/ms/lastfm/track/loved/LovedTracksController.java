package io.github.jonarzz.lastfm2spotify.ms.lastfm.track.loved;

import io.github.jonarzz.lastfm2spotify.commons.dto.LovedTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("tracks/loved")
public class LovedTracksController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LovedTracksController.class);

    private LovedTracksService lovedTracksService;

    LovedTracksController(LovedTracksService lovedTracksService) {
        this.lovedTracksService = lovedTracksService;
    }

    @GetMapping(value = "{username}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<LovedTrack> getLovedTracks(@PathVariable String username) {
        LOGGER.info("Retrieving loved tracks for user: {}", username);
        return lovedTracksService.getLovedTracks(username);
    }

}
