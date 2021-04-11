package io.github.jonarzz.lastfm2spotify.ms.spotify.auth;

import io.github.jonarzz.lastfm2spotify.commons.validation.ValidationPatterns;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;

@Data
class AccessParams {

    @NotNull
    @Pattern(regexp = ValidationPatterns.URL)
    private String redirectUri;
    @NotNull
    @Size(min = 10, max = 50)
    private String correlationId;
    @NotNull
    @Size(min = 1, max = 20)
    private Collection<Scope> scopes;

}

