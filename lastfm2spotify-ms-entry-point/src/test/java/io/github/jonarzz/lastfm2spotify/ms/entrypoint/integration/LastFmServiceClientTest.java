package io.github.jonarzz.lastfm2spotify.ms.entrypoint.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jonarzz.lastfm2spotify.commons.dto.LovedTrack;
import io.github.jonarzz.lastfm2spotify.commons.error.ExternalApiUnavailableException;
import io.github.jonarzz.lastfm2spotify.commons.error.OtherInternalApiException;
import io.github.jonarzz.lastfm2spotify.commons.error.ResourceNotFoundException;
import io.github.jonarzz.lastfm2spotify.ms.entrypoint.integration.IntegrationProperties.MicroserviceConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DisplayName("LastFM microservice client tests")
class LastFmServiceClientTest {

    int stubPort = 8100;

    @RegisterExtension
    StubRunnerExtension stubRunner = new StubRunnerExtension()
            .downloadStub("io.github.jonarzz", "lastfm2spotify-ms-lastfm", "1.0.0", "stubs")
            .withPort(stubPort)
            .stubsMode(StubRunnerProperties.StubsMode.LOCAL);

    private final LastFmServiceClient testedClient;

    LastFmServiceClientTest() {
        MicroserviceConfiguration lastFmConfiguration = new MicroserviceConfiguration();
        lastFmConfiguration.setBaseUrl("http://localhost:%s/".formatted(stubPort));

        IntegrationProperties integrationProperties = new IntegrationProperties();
        integrationProperties.setLastFm(lastFmConfiguration);

        testedClient = new IntegrationConfiguration(integrationProperties)
                .lastFmMicroserviceClient(new ObjectMapper());
    }

    @Nested
    @DisplayName("Get loved tracks")
    class GetLovedTracks {

        @Test
        @DisplayName("User does not exist")
        void userDoesNotExist() {
            String lastFmUsername = "not_existing_user";

            Flux<LovedTrack> result = testedClient.getLovedTracks(lastFmUsername);

            StepVerifier.create(result)
                        .verifyErrorSatisfies(e -> assertThat(e)
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessage("User " + lastFmUsername + " not found"));
        }

        @Test
        @DisplayName("LastFM API unavailable unavailable")
        void lastFmApiUnavailable() {
            Flux<LovedTrack> result = testedClient.getLovedTracks("external_api_500_user");

            StepVerifier.create(result)
                        .verifyErrorSatisfies(e -> assertThat(e)
                                .isInstanceOf(ExternalApiUnavailableException.class)
                                .hasMessage("LastFM API is not unavailable at the moment"));
        }

        @Test
        @DisplayName("Get no tracks")
        void getNoTracks() {
            String lastFmUsername = "no_loved_tracks_user";

            Flux<LovedTrack> result = testedClient.getLovedTracks(lastFmUsername);

            StepVerifier.create(result)
                        .verifyErrorSatisfies(e -> assertThat(e)
                                .isInstanceOf(UserHasNoLovedTracksException.class)
                                .hasMessage("User " + lastFmUsername + " has no loved tracks"));
        }

        @Test
        @DisplayName("Get actual tracks")
        void getActualTracks() {
            Flux<LovedTrack> result = testedClient.getLovedTracks("single_loved_tracks_page_user");

            StepVerifier.create(result)
                        .expectNext(new LovedTrack("Corrosion of Conformity", "Clean My Wounds"))
                        .verifyComplete();
        }

        @Test
        @DisplayName("Other errors handling - invalid remote URL")
        void otherErrorsHandling_invalidRemoteUrl() {
            Flux<LovedTrack> result = testedClient.getLovedTracks("\t");

            StepVerifier.create(result)
                        .verifyErrorSatisfies(e -> assertThat(e)
                                .isInstanceOf(OtherInternalApiException.class)
                                .extracting(Throwable::getMessage)
                                .isNotNull());
        }

    }

}