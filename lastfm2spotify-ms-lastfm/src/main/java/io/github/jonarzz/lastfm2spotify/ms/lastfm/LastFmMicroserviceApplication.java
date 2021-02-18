package io.github.jonarzz.lastfm2spotify.ms.lastfm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(LastFmApiProperties.class)
public class LastFmMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LastFmMicroserviceApplication.class, args);
    }

}
