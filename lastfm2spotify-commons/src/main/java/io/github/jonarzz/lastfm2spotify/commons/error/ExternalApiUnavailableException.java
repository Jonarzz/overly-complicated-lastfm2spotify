package io.github.jonarzz.lastfm2spotify.commons.error;

public class ExternalApiUnavailableException extends RuntimeException {

    public ExternalApiUnavailableException(String message) {
        super(message);
    }

}
