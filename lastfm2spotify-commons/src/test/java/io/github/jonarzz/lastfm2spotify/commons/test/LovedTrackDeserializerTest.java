package io.github.jonarzz.lastfm2spotify.commons.test;

import io.github.jonarzz.lastfm2spotify.commons.dto.LovedTrack;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;

@DisplayName("Loved track deserializer tests")
class LovedTrackDeserializerTest extends BaseDeserializerTest<LovedTrack> {

    protected LovedTrackDeserializerTest() {
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
