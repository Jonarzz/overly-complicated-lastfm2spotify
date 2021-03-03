package io.github.jonarzz.lastfm2spotify.commons.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class LovedTrack {

    private String artist;
    private String title;

}
