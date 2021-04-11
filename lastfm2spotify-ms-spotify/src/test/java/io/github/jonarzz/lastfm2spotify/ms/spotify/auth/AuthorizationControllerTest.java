package io.github.jonarzz.lastfm2spotify.ms.spotify.auth;

import static io.github.jonarzz.lastfm2spotify.commons.test.web.RestDocsConfiguration.documentWithPrettyPrint;
import static io.github.jonarzz.lastfm2spotify.commons.test.web.RestDocsConfiguration.documentWithoutPrettyPrint;
import static io.github.jonarzz.lastfm2spotify.ms.spotify.auth.Scope.PLAYLIST_MODIFY_PRIVATE;
import static io.github.jonarzz.lastfm2spotify.ms.spotify.auth.Scope.PLAYLIST_MODIFY_PUBLIC;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

import io.github.jonarzz.lastfm2spotify.commons.dto.ValidationErrorResponse;
import io.github.jonarzz.lastfm2spotify.commons.validation.ValidationPatterns;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.net.URI;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

@SpringBootTest
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@TestPropertySource(properties = {
        "lastfm2spotify.lastfm-ms.web.accepted-origin-host=localhost",
        "lastfm2spotify.api.spotify.auth.access.base-url=https://accounts.spotify.com/authorize",
        "lastfm2spotify.api.spotify.auth.client-id=a1b2c3d4e5f6g7h8i9j0xyzqwerty123"
})
@DisplayName("Authorization controller tests")
class AuthorizationControllerTest {

    // no way of configuring auto rest docs generation for WebTestClient (see: WebTestClientBasedTest)

    private static final String ACCESS_PATH = "/auth/access";
    private static final String REDIRECT_URI_PARAM = "redirectUri";
    private static final String CORRELATION_ID_PARAM = "correlationId";
    private static final String SCOPES_PARAM = "scopes";

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
    private ApplicationContext context;

    private WebTestClient client;

    private URI correctUri;

    @BeforeAll
    static void beforeAll() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        correctUri = URI.create(accessBaseUrl);

        client = WebTestClient.bindToApplicationContext(context)
                              .configureClient()
                              .filter(documentationConfiguration(restDocumentation))
                              .build();

        when(authorizationService.getAccessUri(argThat(params -> correctRedirectUri.equals(params.getRedirectUri())
                                                                 && params.getCorrelationId().length() >= 10
                                                                 && correctScopes.equals(params.getScopes()))))
                .thenReturn(correctUri);
    }

    @Test
    @DisplayName("Get access URI")
    void getAccessUri() {
        client.get()
              .uri(builder -> builder.path(ACCESS_PATH)
                                     .queryParam(REDIRECT_URI_PARAM, correctRedirectUri)
                                     .queryParam(CORRELATION_ID_PARAM, RandomStringUtils.randomAlphanumeric(10))
                                     .queryParam(SCOPES_PARAM, correctScopesParamValue)
                                     .build())
              .exchange()
              .expectStatus().isOk()
              .expectBody(URI.class)
              .consumeWith(documentWithoutPrettyPrint("auth/access/successful",
                                                      "Content-Type"))
              .isEqualTo(correctUri);
    }

    @Test
    @DisplayName("Apparently not unique correlation ID value")
    void apparentlyNotUniqueCorrelationIdValue() {
        String correlationId = RandomStringUtils.randomAlphanumeric(10);

        client.get()
              .uri(builder -> builder.path(ACCESS_PATH)
                                     .queryParam(REDIRECT_URI_PARAM, correctRedirectUri)
                                     .queryParam(CORRELATION_ID_PARAM, correlationId)
                                     .queryParam(SCOPES_PARAM, correctScopesParamValue)
                                     .build())
              .exchange()
              .expectStatus().isOk();
        client.get()
              .uri(builder -> builder.path(ACCESS_PATH)
                                     .queryParam(REDIRECT_URI_PARAM, correctRedirectUri)
                                     .queryParam(CORRELATION_ID_PARAM, correlationId)
                                     .queryParam(SCOPES_PARAM, correctScopesParamValue)
                                     .build())
              .exchange()
              .expectStatus().isBadRequest()
              .expectBody(ValidationErrorResponse.class)
              .consumeWith(documentWithPrettyPrint("auth/access/notUniqueCorrelationIdParam"))
              .value(response -> assertThat(response)
                      .extracting("message")
                      .asString()
                      .isEqualTo("%s should be unique", CORRELATION_ID_PARAM));
    }

    @Test
    @DisplayName("Invalid correlation ID param value length")
    void invalidCorrelationIdParamValueLength() {
        client.get()
              .uri(builder -> builder.path(ACCESS_PATH)
                                     .queryParam(REDIRECT_URI_PARAM, correctRedirectUri)
                                     .queryParam(CORRELATION_ID_PARAM, RandomStringUtils.randomAlphanumeric(5))
                                     .queryParam(SCOPES_PARAM, correctScopesParamValue)
                                     .build())
              .exchange()
              .expectStatus().isBadRequest()
              .expectBody(ValidationErrorResponse.class)
              .consumeWith(documentWithPrettyPrint("auth/access/invalidCorrelationIdParamLength"))
              .value(response -> assertThat(response)
                      .extracting("message")
                      .asString()
                      .isEqualTo("%s size must be between 10 and 50", CORRELATION_ID_PARAM));
    }

    @Test
    @DisplayName("Invalid redirect URI param value")
    void invalidRedirectUriParamValue() {
        client.get()
              .uri(builder -> builder.path(ACCESS_PATH)
                                     .queryParam(REDIRECT_URI_PARAM, "totally not URI")
                                     .queryParam(CORRELATION_ID_PARAM, RandomStringUtils.randomAlphanumeric(10))
                                     .queryParam(SCOPES_PARAM, correctScopesParamValue)
                                     .build())
              .exchange()
              .expectStatus().isBadRequest()
              .expectBody(ValidationErrorResponse.class)
              .consumeWith(documentWithPrettyPrint("auth/access/invalidRedirectUriParam"))
              .value(response -> assertThat(response)
                      .extracting("message")
                      .asString()
                      .isEqualTo("%s must match \"%s\"", REDIRECT_URI_PARAM, ValidationPatterns.URL));
    }

    @Test
    @DisplayName("Invalid scopes param value")
    @Disabled("The code needs to be fixed first - TODO in the Scope enum")
    void invalidScopesParamValue() {
        client.get()
              .uri(builder -> builder.path(ACCESS_PATH)
                                     .queryParam(REDIRECT_URI_PARAM, correctRedirectUri)
                                     .queryParam(CORRELATION_ID_PARAM, RandomStringUtils.randomAlphanumeric(10))
                                     .queryParam(SCOPES_PARAM, "not a valid scope,")
                                     .build())
              .exchange()
              .expectStatus().isBadRequest()
              .expectBody(ValidationErrorResponse.class)
              .consumeWith(documentWithPrettyPrint("auth/access/invalidScopesParam"))
              .value(response -> assertThat(response)
                      .extracting("message")
                      .asString()
                      .isEqualTo("%s must match \"%s\"", SCOPES_PARAM, ValidationPatterns.URL));
    }

    // TODO other bad request tests + docs

}
