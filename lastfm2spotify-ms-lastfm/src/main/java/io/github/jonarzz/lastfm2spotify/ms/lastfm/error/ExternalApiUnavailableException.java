package io.github.jonarzz.lastfm2spotify.ms.lastfm.error;

public class ExternalApiUnavailableException extends RuntimeException {

    public ExternalApiUnavailableException(String message) {
        super(message);
    }

}
