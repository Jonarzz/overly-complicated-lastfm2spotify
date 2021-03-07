package io.github.jonarzz.lastfm2spotify.ms.lastfm.track.loved;

import static java.util.Collections.singletonList;

import io.github.jonarzz.lastfm2spotify.commons.test.BaseDeserializerTest;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;

@DisplayName("Loved tracks API response deserializer tests")
class LastFmLovedTracksResponseDeserializerTest extends BaseDeserializerTest<LastFmLovedTracksResponse> {

    private static final int PER_PAGE = 50;
    private static final int TOTAL = 73;
    private static final int TOTAL_PAGES = 2;

    protected LastFmLovedTracksResponseDeserializerTest() {
        super(LastFmLovedTracksResponse.class);
    }

    @Override
    protected Map<String, LastFmLovedTracksResponse> jsonToExpectedObject() {
        return Map.ofEntries(
                jsonToExpectedObject(1, "Pink Floyd", "Money"),
                jsonToExpectedObject(2, "Led Zeppelin", "Immigrant song")
        );
    }

    private static Map.Entry<String, LastFmLovedTracksResponse> jsonToExpectedObject(int page, String artist, String title) {
        return Map.entry(createJson(page, artist, title),
                         new LastFmLovedTracksResponse(new PagingMetadata(page, PER_PAGE, TOTAL, TOTAL_PAGES),
                                                       singletonList(new LastFmLovedTrack(artist, title))));
    }

    private static String createJson(int page, String trackArtist, String trackTitle) {
        return """
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
                """.formatted(page, PER_PAGE, TOTAL, TOTAL_PAGES, trackTitle, trackArtist);
    }

}