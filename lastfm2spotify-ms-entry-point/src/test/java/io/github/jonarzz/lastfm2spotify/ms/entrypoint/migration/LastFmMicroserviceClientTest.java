package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import io.github.jonarzz.lastfm2spotify.commons.dto.LovedTrack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DisplayName("LastFM microservice client tests")
class LastFmMicroserviceClientTest {

    int stubPort = 8100;

    @RegisterExtension
    StubRunnerExtension stubRunner = new StubRunnerExtension()
            .downloadStub("io.github.jonarzz", "lastfm2spotify-ms-lastfm", "1.0.0", "stubs")
            .withPort(stubPort)
            .stubsMode(StubRunnerProperties.StubsMode.LOCAL);

    private MigrationConfiguration configuration = new MigrationConfiguration();
    private WebClient lastFmMsClient = configuration.lastFmMsClient("http://localhost:%s".formatted(stubPort));

    private LastFmMicroserviceClient testedClient = new LastFmMicroserviceClient(lastFmMsClient);

    @Nested
    @DisplayName("Get loved tracks")
    class GetLovedTracks {

        @Test
        @DisplayName("User does not exist")
        void userDoesNotExist() {
            Flux<LovedTrack> result = testedClient.getLovedTracks("not_existing_user");

            // TODO add after adding error handling in LastFM MS
        }

        @Test
        @DisplayName("Gateway unavailable")
        void gatewayUnavailable() {
            Flux<LovedTrack> result = testedClient.getLovedTracks("external_api_500_user");

            // TODO add after adding error handling in LastFM MS
        }

        @Test
        @DisplayName("Get no tracks")
        void getNoTracks() {
            Flux<LovedTrack> result = testedClient.getLovedTracks("no_loved_tracks_user");

            StepVerifier.create(result)
                        .verifyComplete();
        }

        @Test
        @DisplayName("Get actual tracks")
        void getActualTracks() {
            Flux<LovedTrack> result = testedClient.getLovedTracks("single_loved_tracks_page_user");

            StepVerifier.create(result)
                        .expectNext(new LovedTrack("Corrosion of Conformity", "Clean My Wounds"))
                        .verifyComplete();
        }

    }

}