package io.github.jonarzz.lastfm2spotify.ms.lastfm.loved;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Loved tracks API response deserializer tests")
class LovedTracksApiResponseDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deserialize response")
    void deserializeResponse() throws JsonProcessingException {
        int page = 1;
        int perPage = 50;
        int total = 73;
        int totalPages = 2;
        String trackTitle = "Pink Floyd";
        String trackArtist = "Money";
        String responseJson = """
                {
                  "lovedtracks": {
                    "@attr": {
                      "page": "%s",
                      "perPage": "%s",
                      "total": "%s",
                      "totalPages": "%s"
                    },
                    "track": [{
                      "name": "%s",
                      "artist": {
                        "name": "%s"
                      }
                    }]
                  }
                }
                """.formatted(page, perPage, total, totalPages, trackTitle, trackArtist);

        LovedTracksApiResponse result = objectMapper.readValue(responseJson, LovedTracksApiResponse.class);

        assertThat(result)
                .as("Deserialized JSON: " + responseJson)
                .isEqualTo(new LovedTracksApiResponse(new PagingMetadata(page, perPage, total, totalPages),
                                                      singletonList(new LovedTrack(trackArtist, trackTitle))));
    }

}