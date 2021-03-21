package io.github.jonarzz.lastfm2spotify.ms.entrypoint.integration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Validated
@ConfigurationProperties(prefix = "lastfm2spotify.integration.ms")
public class IntegrationProperties {

    @NotNull
    private MicroserviceConfiguration lastFm;
    @NotNull
    private MicroserviceConfiguration spotify;

    @Data
    public static class MicroserviceConfiguration {

        @Pattern(regexp = "^https?://.+?/$")
        private String baseUrl;

    }

}
