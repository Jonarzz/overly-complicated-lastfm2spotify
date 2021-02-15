package io.github.jonarzz.lastfm2spotify.ms.lastfm.loved;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("Loved track deserializer tests")
class LovedTrackDeserializerTest {

    private static final String TRACK_JSON_TEMPLATE = """
            {
              "name": "%s",
              "artist": {
                "name": "%s",
                "other_property": "abc"
              },
              "other_property": "xyz"
            }
            """;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deserialize single element")
    void deserializeSingleElement() throws JsonProcessingException {
        String artist = "Metallica";
        String title = "Master of Puppets";
        String trackJson = trackJson(artist, title);

        LovedTrack result = objectMapper.readValue(trackJson, LovedTrack.class);

        assertThat(result)
                .as("Deserialized JSON: " + trackJson)
                .isEqualTo(new LovedTrack(artist, title));
    }

    @Test
    @DisplayName("Deserialize collection")
    void deserializeCollection() throws JsonProcessingException {
        String firstArtist = "Mac Miller";
        String firstTrack = "Diablo";
        String secondArtist = "Venetian Snares";
        String secondTrack = "Hajnal";
        String tracksJson = "[%s, %s]".formatted(trackJson(firstArtist, firstTrack),
                                                 trackJson(secondArtist, secondTrack));

        List<LovedTrack> result = objectMapper.readValue(tracksJson, new TypeReference<>() {});

        assertThat(result)
                .as("Deserialized JSON: " + tracksJson)
                .containsExactly(new LovedTrack(firstArtist, firstTrack),
                                 new LovedTrack(secondArtist, secondTrack));
    }

    private static String trackJson(String artist, String title) {
        return TRACK_JSON_TEMPLATE.formatted(title, artist);
    }

}