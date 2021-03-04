package io.github.jonarzz.lastfm2spotify.commons.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
public class ExceptionWebHandler {

    private static final String ERROR_MESSAGE_KEY = "errorMessage";

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    Map<String, String> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return handleException(exception);
    }

    @ExceptionHandler(ExternalApiUnavailableException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ResponseBody
    Map<String, String> handleExternalApiUnavailableException(ExternalApiUnavailableException exception) {
        return handleException(exception);
    }

    private Map<String, String> handleException(Exception exception) {
        return Map.of(ERROR_MESSAGE_KEY, exception.getMessage());
    }

}
