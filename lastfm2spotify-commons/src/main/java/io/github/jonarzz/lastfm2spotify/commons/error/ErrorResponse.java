package io.github.jonarzz.lastfm2spotify.commons.error;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ErrorResponse {

    private String errorMessage;

}
