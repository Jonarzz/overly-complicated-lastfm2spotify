package io.github.jonarzz.lastfm2spotify.ms.lastfm.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
class ExceptionWebHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionWebHandler.class);

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
        LOGGER.error("{} exception occurred, reason: {}",
                     exception.getClass().getSimpleName(), exception.getMessage());
        return Map.of(ERROR_MESSAGE_KEY, exception.getMessage());
    }

}
