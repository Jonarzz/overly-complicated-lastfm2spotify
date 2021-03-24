package io.github.jonarzz.lastfm2spotify.ms.spotify;

import io.github.jonarzz.lastfm2spotify.commons.test.web.WebTestClientBasedTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;

@AutoConfigureWireMock(port = 0)
@AutoConfigureWebTestClient
@TestPropertySource(properties = {
        "lastfm2spotify.lastfm-ms.web.accepted-origin-host=localhost",
        "lastfm2spotify.api.spotify.auth.access.base-url=https://localhost:${wiremock.server.port}",
        "lastfm2spotify.api.spotify.auth.client-id=a1b2c3d4e5f6g7h8i9j0xyzqwerty123"

})
@DisplayName("Loved tracks API calls")
abstract class SpotifyRestContractTestBase extends WebTestClientBasedTest {

}

