package io.github.jonarzz.lastfm2spotify.ms.entrypoint.playlist;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public class PlaylistToCreate {

    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 30;

    @NotNull
    @Length(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    private String name;
    private boolean publiclyAvailable;
    private SongsOrdering songsOrdering;

    @JsonCreator
    private PlaylistToCreate(String name, Boolean publiclyAvailable, SongsOrdering songsOrdering) {
        this.name = name;
        this.publiclyAvailable = Optional.ofNullable(publiclyAvailable)
                                         .orElse(true);
        this.songsOrdering = Optional.ofNullable(songsOrdering)
                                     .orElse(SongsOrdering.NEW_FIRST);
    }

    public static PlaylistToCreate withName(String name) {
        return new PlaylistToCreate(name, null, null);
    }

    public static PlaylistToCreate withConfiguration(String name, PrivacyConfig privacyConfig, SongsOrdering songsOrdering) {
        return new PlaylistToCreate(name, privacyConfig.isPubliclyAvailable(), songsOrdering);
    }

    public String getName() {
        return name;
    }

    public boolean isPubliclyAvailable() {
        return publiclyAvailable;
    }

    public SongsOrdering getSongsOrdering() {
        return songsOrdering;
    }

    @Override
    public String toString() {
        return "PlaylistToCreate{" +
               "name='" + name + '\'' +
               ", publiclyAvailable=" + publiclyAvailable +
               ", songsOrdering=" + songsOrdering +
               '}';
    }

}
