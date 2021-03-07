package io.github.jonarzz.lastfm2spotify.ms.entrypoint;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "lastfm2spotify.integration.ms")
public class MicroserviceIntegrationProperties {

    @NotNull
    private ServiceInformation lastFm;
    @NotNull
    private ServiceInformation spotify;

    @Getter
    @Setter
    public static class ServiceInformation {

        @NotNull
        @Pattern(regexp = "^https?://.+$")
        private String baseUrl;

    }

}
