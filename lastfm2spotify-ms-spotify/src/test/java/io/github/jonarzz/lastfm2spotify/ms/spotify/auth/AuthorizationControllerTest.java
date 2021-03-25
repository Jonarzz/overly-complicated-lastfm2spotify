package io.github.jonarzz.lastfm2spotify.ms.spotify.auth;

import static io.github.jonarzz.lastfm2spotify.commons.test.web.RestDocsConfiguration.documentWithPrettyPrint;
import static io.github.jonarzz.lastfm2spotify.commons.test.web.RestDocsConfiguration.documentWithoutPrettyPrint;
import static io.github.jonarzz.lastfm2spotify.ms.spotify.auth.Scope.PLAYLIST_MODIFY_PRIVATE;
import static io.github.jonarzz.lastfm2spotify.ms.spotify.auth.Scope.PLAYLIST_MODIFY_PUBLIC;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

import io.github.jonarzz.lastfm2spotify.commons.dto.ValidationErrorResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.net.URI;
import java.util.Collection;
import java.util.Set;

@AutoConfigureRestDocs
@WebFluxTest(AuthorizationController.class)
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@TestPropertySource(properties = {
        "lastfm2spotify.lastfm-ms.web.accepted-origin-host=localhost",
        "lastfm2spotify.api.spotify.auth.access.base-url=https://accounts.spotify.com/authorize",
        "lastfm2spotify.api.spotify.auth.client-id=a1b2c3d4e5f6g7h8i9j0xyzqwerty123"
})
@DisplayName("Authorization controller tests")
class AuthorizationControllerTest {

    // no way of configuring auto rest docs generation for WebTestClient (see: WebTestClientBasedTest)

    private String correctRedirectUri = "http://redirect.com";
    private Collection<Scope> correctScopes = Set.of(PLAYLIST_MODIFY_PUBLIC, PLAYLIST_MODIFY_PRIVATE);
    private String correctScopesParamValue = correctScopes.stream()
                                                          .map(Enum::name)
                                                          .collect(joining(","));

    @MockBean
    private AuthorizationService authorizationService;
    @Value("${lastfm2spotify.api.spotify.auth.access.base-url}")
    private String accessBaseUrl;
    @Autowired
    private WebTestClient client;

    private URI correctUri;

    @BeforeEach
    void setUp() {
        correctUri = URI.create(accessBaseUrl);

        when(authorizationService.getAccessUri(argThat(params -> correctRedirectUri.equals(params.getRedirectUri())
                                                                 && params.getCorrelationId().length() >= 10
                                                                 && correctScopes.equals(params.getScopes()))))
                .thenReturn(correctUri);
    }

    @Test
    @DisplayName("Get access URI")
    void getAccessUri() {
        client.get()
              .uri(builder -> builder.path("auth/access")
                                     .queryParam("redirectUri", correctRedirectUri)
                                     .queryParam("correlationId", RandomStringUtils.randomAlphanumeric(10))
                                     .queryParam("scopes", correctScopesParamValue)
                                     .build())
              .exchange()
              .expectStatus().isOk()
              .expectBody(URI.class)
              .consumeWith(documentWithoutPrettyPrint("auth/access/successful",
                                                      "Content-Type"))
              .isEqualTo(correctUri);
    }

    @Test
    @DisplayName("Apparently not unique correlation ID")
    void apparentlyNotUniqueCorrelationId() {
        String correlationId = RandomStringUtils.randomAlphanumeric(10);

        client.get()
              .uri(builder -> builder.path("auth/access")
                                     .queryParam("redirectUri", correctRedirectUri)
                                     .queryParam("correlationId", correlationId)
                                     .queryParam("scopes", correctScopesParamValue)
                                     .build())
              .exchange()
              .expectStatus().isOk();
        client.get()
              .uri(builder -> builder.path("auth/access")
                                     .queryParam("redirectUri", correctRedirectUri)
                                     .queryParam("correlationId", correlationId)
                                     .queryParam("scopes", correctScopesParamValue)
                                     .build())
              .exchange()
              .expectStatus().isBadRequest()
              .expectBody(ValidationErrorResponse.class)
              .consumeWith(documentWithPrettyPrint("auth/access/notUniqueCorrelationId"))
              .value(response -> assertThat(response)
                      .extracting("message")
                      .isEqualTo("Correlation ID should be unique"));
    }

    // TODO other bad request tests + docs

}
