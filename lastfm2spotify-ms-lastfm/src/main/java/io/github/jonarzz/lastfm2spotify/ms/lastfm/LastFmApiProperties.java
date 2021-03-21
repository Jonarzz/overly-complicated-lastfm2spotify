package io.github.jonarzz.lastfm2spotify.ms.lastfm;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Validated
@ConfigurationProperties(prefix = "lastfm2spotify.api.lastfm")
public class LastFmApiProperties {

    @NotNull
    @Pattern(regexp = "^https?://.+$")
    private String baseUrl;
    @NotNull
    @Pattern(regexp = "[a-z0-9]{32}")
    private String apiKey;
    @Range(min = 1, max = 1000)
    private int singlePageLimit = 50;

}
