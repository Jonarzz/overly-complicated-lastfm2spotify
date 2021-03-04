package io.github.jonarzz.lastfm2spotify.ms.lastfm.error;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
