package io.github.jonarzz.lastfm2spotify.ms.entrypoint.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@CrossOrigin("${lastfm2spotify.entrypoint-ms.web.accepted-origin-host}")
public class AuthorizationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationController.class);

    private AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping("spotify")
    public ResponseEntity<Void> getSpotifyLoginUri() {
        LOGGER.info("Spotify login URI requested");
        return ResponseEntity.status(HttpStatus.FOUND)
                             .location(authorizationService.getSpotifyLoginUri())
                             .build();
    }

}
