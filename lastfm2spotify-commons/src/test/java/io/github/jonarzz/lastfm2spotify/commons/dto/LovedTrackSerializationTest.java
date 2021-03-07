package io.github.jonarzz.lastfm2spotify.commons.dto;

import io.github.jonarzz.lastfm2spotify.commons.test.BaseSerializationTest;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;

@DisplayName("Loved track serialization tests")
class LovedTrackSerializationTest extends BaseSerializationTest<LovedTrack> {

    protected LovedTrackSerializationTest() {
        super(LovedTrack.class);
    }

    @Override
    protected Map<String, LovedTrack> jsonToExpectedObject() {
        return Map.ofEntries(
                jsonToExpectedObject("Metallica", "Master of Puppets"),
                jsonToExpectedObject("Mac Miller", "Diablo")
        );
    }

    private static Map.Entry<String, LovedTrack> jsonToExpectedObject(String artist, String title) {
        return Map.entry(createJson(artist, title),
                         new LovedTrack(artist, title));
    }

    private static String createJson(String artist, String title) {
        return """
                {
                  "artist": "%s",
                  "title": "%s"
                }
                """.formatted(artist, title);
    }

}
