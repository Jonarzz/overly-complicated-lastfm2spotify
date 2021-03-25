package io.github.jonarzz.lastfm2spotify.ms.spotify;

import static java.util.stream.Collectors.joining;

import io.github.jonarzz.lastfm2spotify.ms.spotify.auth.AuthorizationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.web.reactive.function.server.ServerRequest;
import java.util.Map;

@SpringBootApplication
@EnableConfigurationProperties(AuthorizationProperties.class)
public class SpotifyMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotifyMicroserviceApplication.class, args);
    }

    @Bean
    public ErrorAttributes errorAttributes() {
        // TODO move to a more appropriate class
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);
                Throwable error = getError(request);
                if (error instanceof BindingResult) {
                    String message = ((BindingResult) error).getAllErrors()
                                                            .stream()
                                                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                                            .collect(joining(". "));
                    errorAttributes.put("message", message);
                }
                return errorAttributes;
            }
        };
    }

}
