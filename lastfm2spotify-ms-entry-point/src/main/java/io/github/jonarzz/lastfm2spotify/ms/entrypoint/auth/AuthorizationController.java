package io.github.jonarzz.lastfm2spotify.ms.entrypoint.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("auth")
@CrossOrigin("${lastfm2spotify.web.accepted-origin-host}")
public class AuthorizationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationController.class);

    @GetMapping("spotify")
    public ResponseEntity<Void> loginToSpotify() {
        LOGGER.info("Spotify login URI requested");
        return ResponseEntity.status(HttpStatus.FOUND)
                             // TODO to be replaced with Spotify login URI (from ms-spotify call)
                             .location(URI.create("https://accounts.spotify.com/pl/authorize?client_id=123"))
                             .build();
    }

}
