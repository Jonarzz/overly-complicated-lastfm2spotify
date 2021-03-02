package io.github.jonarzz.lastfm2spotify.ms.lastfm.loved;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
public class LovedTrack {

    private String artist;
    private String title;

}
