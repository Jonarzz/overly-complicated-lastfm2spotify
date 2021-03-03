package io.github.jonarzz.lastfm2spotify.ms.lastfm.track.loved;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.github.jonarzz.lastfm2spotify.commons.dto.LovedTrack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

@DisplayName("Loved track deserializer tests")
class LovedTrackDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest(name = "{0}")
    @MethodSource("argsProvider")
    @DisplayName("Deserialize single element")
    <T> void deserializeSingleElement(Class<T> testedClass,
                                      BiFunction<String, String, String> jsonCreator,
                                      BiFunction<String, String, T> expectedObjectCreator) throws JsonProcessingException {
        String artist = "Metallica";
        String title = "Master of Puppets";
        String trackJson = jsonCreator.apply(artist, title);

        T result = objectMapper.readValue(trackJson, testedClass);

        assertThat(result)
                .as("Deserialized JSON: " + trackJson)
                .isEqualTo(expectedObjectCreator.apply(artist, title));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("argsProvider")
    @DisplayName("Deserialize collection")
    <T> void deserializeCollection(Class<T> testedClass,
                                   BiFunction<String, String, String> jsonCreator,
                                   BiFunction<String, String, T> expectedObjectCreator) throws JsonProcessingException {
        String firstArtist = "Mac Miller";
        String firstTrack = "Diablo";
        String secondArtist = "Venetian Snares";
        String secondTrack = "Hajnal";
        String tracksJson = "[%s, %s]" .formatted(jsonCreator.apply(firstArtist, firstTrack),
                                                  jsonCreator.apply(secondArtist, secondTrack));
        CollectionType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, testedClass);

        List<T> result = objectMapper.readValue(tracksJson, collectionType);

        assertThat(result)
                .as("Deserialized JSON: " + tracksJson)
                .containsExactly(expectedObjectCreator.apply(firstArtist, firstTrack),
                                 expectedObjectCreator.apply(secondArtist, secondTrack));
    }

    private static Stream<Arguments> argsProvider() {
        BiFunction<String, String, String> lastFmJsonCreator = LovedTrackDeserializerTest::lastFmJson;
        BiFunction<String, String, LastFmLovedTrack> lastFmObjectCreator = LastFmLovedTrack::new;
        BiFunction<String, String, String> internalJsonCreator = LovedTrackDeserializerTest::internalJson;
        BiFunction<String, String, LovedTrack> internalObjectCreator = LovedTrack::new;
        return Stream.of(
                // tested class            json creator         expected object creator
                of(LastFmLovedTrack.class, lastFmJsonCreator,   lastFmObjectCreator),
                of(LovedTrack.class,       internalJsonCreator, internalObjectCreator)
        );
    }

    private static String lastFmJson(String artist, String title) {
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

    private static String internalJson(String artist, String title) {
        return """
                    {
                      "artist": "%s",
                      "title": "%s"
                    }
                    """.formatted(artist, title);
    }

}