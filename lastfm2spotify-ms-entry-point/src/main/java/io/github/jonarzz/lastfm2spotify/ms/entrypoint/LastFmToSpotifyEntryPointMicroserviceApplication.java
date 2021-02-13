package io.github.jonarzz.lastfm2spotify.ms.entrypoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class LastFmToSpotifyEntryPointMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LastFmToSpotifyEntryPointMicroserviceApplication.class, args);
    }

}
