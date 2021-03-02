package io.github.jonarzz.lastfm2spotify.ms.lastfm.loved;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonDeserialize(using = LastFmLovedTrackDeserializer.class)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
@ToString
class LastFmLovedTrack {

    private String artist;
    private String title;

    LovedTrack toInternalModel() {
        return new LovedTrack(artist, title);
    }

}
