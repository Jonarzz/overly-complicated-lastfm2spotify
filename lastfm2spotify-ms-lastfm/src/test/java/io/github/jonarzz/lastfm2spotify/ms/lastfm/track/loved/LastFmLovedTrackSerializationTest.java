package io.github.jonarzz.lastfm2spotify.ms.lastfm.track.loved;

import io.github.jonarzz.lastfm2spotify.commons.test.BaseSerializationTest;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;

@DisplayName("LastFM loved track serialization tests")
class LastFmLovedTrackSerializationTest extends BaseSerializationTest<LastFmLovedTrack> {

    protected LastFmLovedTrackSerializationTest() {
        super(LastFmLovedTrack.class);
    }

    @Override
    protected Map<String, LastFmLovedTrack> jsonToExpectedObject() {
        return Map.ofEntries(
                jsonToExpectedObject("Metallica", "Master of Puppets"),
                jsonToExpectedObject("Mac Miller", "Diablo")
        );
    }

    @Override
    protected boolean shouldOnlyTestDeserialization() {
        return true;
    }

    private static Map.Entry<String, LastFmLovedTrack> jsonToExpectedObject(String artist, String title) {
        return Map.entry(createJson(artist, title),
                         new LastFmLovedTrack(artist, title));
    }

    private static String createJson(String artist, String title) {
        return """
                {
                  "name": "%s",
                  "artist": {
                    "name": "%s",
                    "other_property": "abc"
                  },
                  "other_property": "xyz"
                }
                """.formatted(title, artist);
    }

}