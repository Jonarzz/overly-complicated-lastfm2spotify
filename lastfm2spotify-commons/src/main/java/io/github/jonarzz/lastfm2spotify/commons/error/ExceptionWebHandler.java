package io.github.jonarzz.lastfm2spotify.commons.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionWebHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse handleResourceNotFoundException(ResourceNotFoundException exception) {
        return createResponse(exception);
    }

    @ExceptionHandler(ExternalApiUnavailableException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    ErrorResponse handleExternalApiUnavailableException(ExternalApiUnavailableException exception) {
        return createResponse(exception);
    }

    @ExceptionHandler(OtherInternalApiException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorResponse handleOtherInternalApiException(OtherInternalApiException exception) {
        return createResponse(exception);
    }

    private ErrorResponse createResponse(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

}
