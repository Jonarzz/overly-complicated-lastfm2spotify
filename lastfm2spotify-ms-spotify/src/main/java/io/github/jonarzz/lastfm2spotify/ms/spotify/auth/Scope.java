package io.github.jonarzz.lastfm2spotify.ms.spotify.auth;

import static java.util.stream.Collectors.joining;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

public enum Scope {

    PLAYLIST_MODIFY_PUBLIC("playlist-modify-public"),
    PLAYLIST_MODIFY_PRIVATE("playlist-modify-private");

    private static final String ACCEPTABLE_VALUES_STRING = Arrays.stream(values())
                                                                 .map(Enum::name)
                                                                 .collect(joining(", "));

    private String spotifyValue;

    Scope(String spotifyValue) {
        this.spotifyValue = spotifyValue;
    }

    @JsonCreator // TODO StringToEnumConverterFactory not calling this method, but Enum.valueOf - additional configuration needed
    public static Scope fromString(String value) {
        if (value == null) {
            return null;
        }
        String letterOnlyValue = value.replaceAll("[_\\-]", "");
        return Arrays.stream(values())
                     .filter(enumValue -> enumValue.name()
                                                   .replace("_", "")
                                                   .equalsIgnoreCase(letterOnlyValue))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("Acceptable scope values are: " + ACCEPTABLE_VALUES_STRING));
    }

    String spotifyValue() {
        return spotifyValue;
    }

}
