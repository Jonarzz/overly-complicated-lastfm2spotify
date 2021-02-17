package io.github.jonarzz.lastfm2spotify.ms.lastfm.loved;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@JsonDeserialize(using = LovedTracksApiResponseDeserializer.class)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
@EqualsAndHashCode
@ToString
class LovedTracksApiResponse {

    private PagingMetadata pagingMetadata;
    private List<LovedTrack> lovedTracks;

}