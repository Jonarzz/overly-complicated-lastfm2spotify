package io.github.jonarzz.lastfm2spotify.ms.entrypoint.integration;

import io.github.jonarzz.lastfm2spotify.commons.dto.LovedTrack;
import io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist.CreatedPlaylist;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Optional;

public class SpotifyMicroserviceClient {

    private WebClient webClient;

    SpotifyMicroserviceClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Optional<String> getSpotifyLoginUri() {
        return Optional.empty();
    }

    public void createPlaylist(CreatedPlaylist playlistInCreation) {
        // TODO call ms-spotify POST /playlist
        playlistInCreation.created("id TODO", "url TODO");
    }

    public boolean addTrack(CreatedPlaylist createdPlaylist, LovedTrack lovedTrack) {
        // TODO call ms-spotify GET /track?query="..."
        webClient.get()
                 .uri("TODO", lovedTrack.getArtist(), lovedTrack.getTitle());
        // TODO call ms-spotify POST /playlist/{id}/{trackId}
        webClient.post()
                 .uri("TODO", createdPlaylist.getPlaylistData().getName());
        return true;
    }

}
