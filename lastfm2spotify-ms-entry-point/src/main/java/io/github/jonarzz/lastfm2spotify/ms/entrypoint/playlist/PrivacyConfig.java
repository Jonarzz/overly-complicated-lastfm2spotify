package io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist;

public enum PrivacyConfig {

    PUBLIC(true),
    PRIVATE(false);

    private boolean publiclyAvailable;

    PrivacyConfig(boolean publiclyAvailable) {
        this.publiclyAvailable = publiclyAvailable;
    }

    boolean isPubliclyAvailable() {
        return publiclyAvailable;
    }

}
