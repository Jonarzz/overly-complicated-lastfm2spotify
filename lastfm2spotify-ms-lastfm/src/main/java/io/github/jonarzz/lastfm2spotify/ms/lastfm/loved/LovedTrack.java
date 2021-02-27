package io.github.jonarzz.lastfm2spotify.ms.lastfm.loved;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonDeserialize(using = LovedTrackDeserializer.class)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
@ToString
class LovedTrack {

    private String artist;
    private String title;

}