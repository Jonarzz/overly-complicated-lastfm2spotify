package io.github.jonarzz.lastfm2spotify.ms.spotify;

import io.github.jonarzz.lastfm2spotify.ms.spotify.auth.AuthorizationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AuthorizationProperties.class)
public class SpotifyMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotifyMicroserviceApplication.class, args);
    }

}
