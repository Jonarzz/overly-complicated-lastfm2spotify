package io.github.jonarzz.lastfm2spotify.ms.spotify.auth;

public enum Scope {

    PLAYLIST_MODIFY_PUBLIC("playlist-modify-public"),
    PLAYLIST_MODIFY_PRIVATE("playlist-modify-private");

    private String spotifyValue;

    Scope(String spotifyValue) {
        this.spotifyValue = spotifyValue;
    }

    String spotifyValue() {
        return spotifyValue;
    }

}
