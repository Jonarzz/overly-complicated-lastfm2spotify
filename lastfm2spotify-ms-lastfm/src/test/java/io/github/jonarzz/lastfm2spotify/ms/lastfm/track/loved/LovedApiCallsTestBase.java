package io.github.jonarzz.lastfm2spotify.ms.lastfm.track.loved;

import static io.github.jonarzz.lastfm2spotify.commons.test.RestDocsConfiguration.documentWithoutPrettyPrint;
import static java.time.Duration.ofSeconds;

import io.github.jonarzz.lastfm2spotify.commons.dto.LovedTrack;
import io.github.jonarzz.lastfm2spotify.commons.test.WebTestClientBasedTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@AutoConfigureWireMock(port = 0)
@AutoConfigureWebTestClient
@TestPropertySource(properties = {
        "lastfm2spotify.api.lastfm.base-url=http://localhost:${wiremock.server.port}",
        "lastfm2spotify.api.lastfm.api-key=a1b2c3d4e5f6g7h8i9j0xyzqwerty123",
        "lastfm2spotify.api.lastfm.single-page-limit=1",
        "logging.level.io.github.jonarzz=debug"

})
@DisplayName("Loved tracks API calls")
abstract class LovedApiCallsTestBase extends WebTestClientBasedTest {

    @Autowired
    private WebTestClient client;

    @Test
    @DisplayName("Loved tracks are fetched across multiple pages")
    void lovedTracksAreFetchedAcrossMultiplePages() {
        client.get()
              .uri("tracks/loved/multiple_loved_tracks_pages_user")
              .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_NDJSON_VALUE)
              .exchange()
              .expectStatus().isOk()
              .expectHeader().contentType(MediaType.APPLICATION_NDJSON)
              .expectBodyList(LovedTrack.class)
              .consumeWith(documentWithoutPrettyPrint("loved/multipleResults"))
              .hasSize(3)
              .contains(new LovedTrack("Corrosion of Conformity", "Clean My Wounds"),
                        new LovedTrack("Florence Black",          "Bird On A Chain"),
                        new LovedTrack("A Tribe Called Quest",    "Scenario"));
    }

    @Test
    @DisplayName("Loved tracks are fetched across multiple pages - LastFM API results returned with delay")
    void lovedTracksAreFetchedAcrossMultiplePages_lastFmApiResultsReturnedWithDelay() {
        Flux<LovedTrack> response = client.mutate()
                                          .responseTimeout(ofSeconds(1))
                                          .build()
                                          .get()
                                          .uri("tracks/loved/multiple_loved_tracks_pages_with_delay_user")
                                          .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_NDJSON_VALUE)
                                          .exchange()
                                          .expectStatus().isOk()
                                          .expectHeader().contentType(MediaType.APPLICATION_NDJSON)
                                          .returnResult(LovedTrack.class)
                                          .getResponseBody();

        StepVerifier.create(response)
                    .expectNext(new LovedTrack("Corrosion of Conformity", "Clean My Wounds"))
                    .expectNext(new LovedTrack("Florence Black",          "Bird On A Chain"))
                    .expectNext(new LovedTrack("A Tribe Called Quest",    "Scenario"))
                    .verifyComplete();
    }

    @Nested
    @DisplayName("Contract-based tests duplicates for docs purposes")
    class ContractsBasedDuplicatesForDocsPurposes {

        // no way of configuring auto rest docs generation for WebTestClient (see superclass)

        @Test
        @DisplayName("User with no loved tracks")
        void userWithNoLovedTracks() {
            client.get()
                  .uri("tracks/loved/no_loved_tracks_user")
                  .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_NDJSON_VALUE)
                  .exchange()
                  .expectStatus().isNoContent()
                  .expectBody()
                  .consumeWith(documentWithoutPrettyPrint("loved/userWithNoLovedTracks",
                                                       "Content-Type"));
        }

        @Test
        @DisplayName("No LastFM user found")
        void noLastFmUserFound() {
            client.get()
                  .uri("tracks/loved/not_existing_user")
                  .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_NDJSON_VALUE)
                  .exchange()
                  .expectStatus().isNotFound()
                  .expectBody()
                  .consumeWith(documentWithoutPrettyPrint("loved/userNotFound"));
        }

        @Test
        @DisplayName("LastFM API unavailable")
        void lastFmApiUnavailable() {
            client.get()
                  .uri("tracks/loved/external_api_500_user")
                  .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_NDJSON_VALUE)
                  .exchange()
                  .expectStatus().isEqualTo(HttpStatus.BAD_GATEWAY)
                  .expectBody()
                  .consumeWith(documentWithoutPrettyPrint("loved/lastFmApiUnavailable"));
        }

    }

}
