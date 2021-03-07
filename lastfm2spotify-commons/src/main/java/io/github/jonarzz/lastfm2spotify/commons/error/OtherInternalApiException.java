package io.github.jonarzz.lastfm2spotify.commons.error;

public class OtherInternalApiException extends RuntimeException {

    public OtherInternalApiException(String message) {
        super(message);
    }

    public OtherInternalApiException(String message, Throwable cause) {
        super(message, cause);
    }

}
