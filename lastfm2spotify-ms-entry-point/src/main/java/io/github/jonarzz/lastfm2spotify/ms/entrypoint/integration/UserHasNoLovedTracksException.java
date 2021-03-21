package io.github.jonarzz.lastfm2spotify.ms.entrypoint.integration;

class UserHasNoLovedTracksException extends RuntimeException {

    UserHasNoLovedTracksException(String message) {
        super(message);
    }

}
