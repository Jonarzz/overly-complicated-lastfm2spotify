package io.github.jonarzz.lastfm2spotify.ms.lastfm.loved;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Objects;

@JsonDeserialize(using = LovedTrackDeserializer.class)
class LovedTrack {

    private String artist;
    private String title;

    LovedTrack(String artist, String title) {
        this.artist = artist;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LovedTrack)) {
            return false;
        }
        LovedTrack that = (LovedTrack) o;
        return Objects.equal(artist, that.artist) &&
               Objects.equal(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(artist, title);
    }

    @Override
    public String toString() {
        return "LovedTrack{" +
               "artist='" + artist + '\'' +
               ", title='" + title + '\'' +
               '}';
    }

}
