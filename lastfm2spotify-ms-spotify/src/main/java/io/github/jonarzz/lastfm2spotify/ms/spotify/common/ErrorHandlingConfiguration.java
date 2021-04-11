package io.github.jonarzz.lastfm2spotify.ms.spotify.common;

import static java.util.stream.Collectors.joining;

import com.google.common.base.Throwables;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.reactive.function.server.ServerRequest;
import java.util.Map;

@Configuration
class ErrorHandlingConfiguration {

    @Bean
    ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);
                Throwable requestError = getError(request);
                if (requestError instanceof BindingResult) {
                    String message = ((BindingResult) requestError).getAllErrors()
                                                                   .stream()
                                                                   .map(ErrorHandlingConfiguration::getDefaultMessage)
                                                                   .collect(joining(". "));
                    errorAttributes.put("message", message);
                }
                return errorAttributes;
            }
        };
    }

    private static String getDefaultMessage(ObjectError error) {
        if (error instanceof FieldError fieldError) {
            if (fieldError.contains(Exception.class)) {
                Throwable cause = Throwables.getRootCause(fieldError.unwrap(Exception.class));
                return cause.getMessage();
            }
            return fieldError.getField() + " " + error.getDefaultMessage();
        }
        return error.getDefaultMessage();
    }

}
