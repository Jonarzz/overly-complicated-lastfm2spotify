package io.github.jonarzz.lastfm2spotify.ms.lastfm.loved;

import io.github.jonarzz.lastfm2spotify.commons.test.WebTestClientContractTestBase;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;

@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "lastfm2spotify.api.lastfm.base-url=http://localhost:${wiremock.server.port}",
        "lastfm2spotify.api.lastfm.api-key=a1b2c3d4e5f6g7h8i9j0xyzqwerty123",
        "lastfm2spotify.api.lastfm.single-page-limit=1"

})
public abstract class LovedApiCallsTestBase extends WebTestClientContractTestBase {

}
