package io.github.jonarzz.lastfm2spotify.ms.spotify.common;

import io.github.jonarzz.lastfm2spotify.commons.validation.ValidationPatterns;
import lombok.Data;
import javax.validation.constraints.Pattern;

@Data
public class EndpointProperties {

    @Pattern(regexp = ValidationPatterns.SECURE_URL)
    private String baseUrl;

}
