package io.github.jonarzz.lastfm2spotify.ms.entrypoint;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Validated
@ConfigurationProperties(prefix = "lastfm2spotify.integration")
public class IntegrationProperties {

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "lastfm")
    public static class LastFm {

        @NotNull
        @Pattern(regexp = "^https?://.+$")
        private String baseUrl;

    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "spotify")
    public static class Spotify {

        @NotNull
        @Pattern(regexp = "^https?://.+$")
        private String baseUrl;

    }
}
