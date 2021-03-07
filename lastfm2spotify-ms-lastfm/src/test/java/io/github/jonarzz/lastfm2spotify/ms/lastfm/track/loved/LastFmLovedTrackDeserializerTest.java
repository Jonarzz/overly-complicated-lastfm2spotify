package io.github.jonarzz.lastfm2spotify.ms.lastfm.track.loved;

import io.github.jonarzz.lastfm2spotify.commons.test.BaseDeserializerTest;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;

@DisplayName("Loved track deserializer tests")
class LastFmLovedTrackDeserializerTest extends BaseDeserializerTest<LastFmLovedTrack> {

    protected LastFmLovedTrackDeserializerTest() {
        super(LastFmLovedTrack.class);
    }

    @Override
    protected Map<String, LastFmLovedTrack> jsonToExpectedObject() {
        return Map.ofEntries(
                jsonToExpectedObject("Metallica", "Master of Puppets"),
                jsonToExpectedObject("Mac Miller", "Diablo")
        );
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