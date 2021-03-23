package io.github.jonarzz.lastfm2spotify.commons.validation;

public class ValidationPatterns {

    public static final String URL = "^https?://.+$";
    public static final String SECURE_URL = "^https://.+$";
    public static final String KEY_HASH = "^[a-z0-9]{32}$";
    public static final String AT_LEAST_10_CHARS = "^.{10,}$";

    private ValidationPatterns() {
    }

}
