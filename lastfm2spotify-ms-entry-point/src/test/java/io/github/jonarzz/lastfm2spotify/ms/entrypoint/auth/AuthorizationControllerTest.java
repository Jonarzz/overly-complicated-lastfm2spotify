package io.github.jonarzz.lastfm2spotify.ms.entrypoint.auth;

import static io.github.jonarzz.lastfm2spotify.commons.test.RestDocsConfiguration.documentWithPrettyPrint;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@DisplayName("Authorization controller tests")
@WebFluxTest(AuthorizationController.class)
@TestPropertySource(properties = "lastfm2spotify.web.accepted-origin-host=localhost")
@AutoConfigureRestDocs
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
class AuthorizationControllerTest {

    @Autowired
    private WebTestClient client;

    @Test
    @DisplayName("Login to Spotify (redirection)")
    void loginToSpotify() {
        client.get()
              .uri("/auth/spotify")
              .exchange()
              .expectStatus().isFound()
              .expectHeader().location("https://accounts.spotify.com/pl/authorize?client_id=123")
              .expectBody()
              .consumeWith(documentWithPrettyPrint("spotifyAuth"));
    }

}