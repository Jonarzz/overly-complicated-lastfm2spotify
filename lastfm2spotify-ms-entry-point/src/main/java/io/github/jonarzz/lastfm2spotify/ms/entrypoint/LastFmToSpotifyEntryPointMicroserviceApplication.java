package io.github.jonarzz.lastfm2spotify.ms.entrypoint;

import io.github.jonarzz.lastfm2spotify.commons.error.ExceptionWebHandler;
import io.github.jonarzz.lastfm2spotify.ms.entrypoint.integration.IntegrationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(IntegrationProperties.class)
public class LastFmToSpotifyEntryPointMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LastFmToSpotifyEntryPointMicroserviceApplication.class, args);
    }

    @Bean
    ExceptionWebHandler exceptionWebHandler() {
        return new ExceptionWebHandler();
    }

}
