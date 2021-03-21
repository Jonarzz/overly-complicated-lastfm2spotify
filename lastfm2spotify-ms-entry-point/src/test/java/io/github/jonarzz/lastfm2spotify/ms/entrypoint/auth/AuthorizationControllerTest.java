package io.github.jonarzz.lastfm2spotify.ms.entrypoint.auth;

import static io.github.jonarzz.lastfm2spotify.commons.test.web.RestDocsConfiguration.documentWithoutPrettyPrint;
import static org.mockito.Mockito.when;

import io.github.jonarzz.lastfm2spotify.ms.entrypoint.testutil.DocumentedControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.net.URI;

@DocumentedControllerTest
@DisplayName("Authorization controller tests")
@WebFluxTest(AuthorizationController.class)
class AuthorizationControllerTest {

    @MockBean
    private AuthorizationService authorizationService;
    @Autowired
    private WebTestClient client;

    @Test
    @DisplayName("Get Spotify login URL")
    void getSpotifyLoginUrl() {
        String loginUrl = "https://accounts.spotify.com/pl/authorize?client_id=123";
        when(authorizationService.getSpotifyLoginUrl())
                .thenReturn(URI.create(loginUrl));

        client.get()
              .uri("/auth/spotify")
              .exchange()
              .expectStatus().isFound()
              .expectHeader().location(loginUrl)
              .expectBody(Void.class)
              .consumeWith(documentWithoutPrettyPrint("spotifyLoginUrl"));
    }

}
