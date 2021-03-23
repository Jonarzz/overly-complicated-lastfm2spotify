package io.github.jonarzz.lastfm2spotify.ms.spotify.auth;

import io.github.jonarzz.lastfm2spotify.commons.validation.ValidationPatterns;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.net.URI;
import java.util.Collection;

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
    public URI getAccessUri(@RequestParam @Pattern(regexp = ValidationPatterns.URL) String redirectUri,
                     @RequestParam @Pattern(regexp = ValidationPatterns.AT_LEAST_10_CHARS) String correlationId,
                     @RequestParam @Size(min = 1, max = 20) Collection<Scope> scopes) {
        // TODO testy (z bledami walidacji)
        return authorizationService.getAccessUri(redirectUri, correlationId, scopes);
    }

}
