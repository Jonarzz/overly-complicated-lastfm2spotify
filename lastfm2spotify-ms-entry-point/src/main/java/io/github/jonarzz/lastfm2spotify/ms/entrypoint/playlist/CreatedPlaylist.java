package io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist;

import lombok.Getter;

@Getter
public class CreatedPlaylist {

    private PlaylistToCreate playlistData;
    private String id;
    private String url;

    private CreatedPlaylist(PlaylistToCreate playlistData) {
        this.playlistData = playlistData;
    }

    public static CreatedPlaylist beforeCreation(PlaylistToCreate playlistData) {
        return new CreatedPlaylist(playlistData);
    }

    public CreatedPlaylist created(String id, String url) {
        this.id = id;
        this.url = url;
        return this;
    }

}
