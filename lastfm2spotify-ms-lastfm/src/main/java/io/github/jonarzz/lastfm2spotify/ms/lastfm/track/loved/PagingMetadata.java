package io.github.jonarzz.lastfm2spotify.ms.lastfm.track.loved;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
@EqualsAndHashCode
@ToString
class PagingMetadata {

    private int page;
    private int perPage;
    private int total;
    private int totalPages;

}
