package io.github.jonarzz.lastfm2spotify.ms.spotify.auth;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;

@Validated
@RestController
@RequestMapping("auth")
@CrossOrigin("${lastfm2spotify.spotify-ms.web.accepted-origin-host}")
public class AuthorizationController {

    private AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping("access")
    public URI getAccessUri(AccessParams accessParams) {
        return authorizationService.getAccessUri(accessParams);
    }

}
