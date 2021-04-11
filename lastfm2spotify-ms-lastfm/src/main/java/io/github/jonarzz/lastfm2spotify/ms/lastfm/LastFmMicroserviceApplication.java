package io.github.jonarzz.lastfm2spotify.ms.lastfm;

import io.github.jonarzz.lastfm2spotify.commons.error.ExceptionWebHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import java.util.Locale;

@SpringBootApplication
@EnableConfigurationProperties(LastFmApiProperties.class)
public class LastFmMicroserviceApplication {

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        SpringApplication.run(LastFmMicroserviceApplication.class, args);
    }

    @Bean
    ExceptionWebHandler exceptionWebHandler() {
        return new ExceptionWebHandler();
    }

}
