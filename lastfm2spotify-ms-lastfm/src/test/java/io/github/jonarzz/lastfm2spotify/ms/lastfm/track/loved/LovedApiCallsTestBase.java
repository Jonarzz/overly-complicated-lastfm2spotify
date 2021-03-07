package io.github.jonarzz.lastfm2spotify.ms.lastfm.track.loved;

import static java.time.Duration.ofSeconds;

import io.github.jonarzz.lastfm2spotify.commons.dto.LovedTrack;
import io.github.jonarzz.lastfm2spotify.commons.test.WebTestClientContractTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
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
abstract class LovedApiCallsTestBase extends WebTestClientContractTestBase {

    @Autowired
    private WebTestClient client;

    @Test
    @DisplayName("Loved tracks are fetched across multiple pages")
    void lovedTracksAreFetchedAcrossMultiplePages() {
        Flux<LovedTrack> response = client.get()
                                          .uri("tracks/loved/multiple_loved_tracks_pages_user")
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

    @Test
    @DisplayName("Loved tracks are fetched across multiple pages - LastFM API results returned with delay")
    void lovedTracksAreFetchedAcrossMultiplePages_lastFmApiResultsReturnedWithDelay() {
        Flux<LovedTrack> response = client.mutate()
                                          .responseTimeout(ofSeconds(1))
                                          .build()
                                          .get()
                                          .uri("tracks/loved/multiple_loved_tracks_pages_with_delay_user")
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

}
