package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

class UserHasNoLovedTracksException extends RuntimeException {

    UserHasNoLovedTracksException(String message) {
        super(message);
    }

}
