package io.github.jonarzz.lastfm2spotify.ms.spotify.auth;

import io.github.jonarzz.lastfm2spotify.commons.validation.ValidationPatterns;
import io.github.jonarzz.lastfm2spotify.ms.spotify.common.EndpointProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Validated
@ConfigurationProperties(prefix = "lastfm2spotify.api.spotify.auth")
public class AuthorizationProperties {

    @NotNull
    @Pattern(regexp = ValidationPatterns.KEY_HASH)
    private String clientId;

    @NotNull
    private EndpointProperties access;

}
