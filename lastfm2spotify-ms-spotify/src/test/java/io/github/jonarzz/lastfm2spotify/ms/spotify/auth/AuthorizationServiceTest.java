package io.github.jonarzz.lastfm2spotify.ms.spotify.auth;

import static io.github.jonarzz.lastfm2spotify.ms.spotify.auth.Scope.PLAYLIST_MODIFY_PRIVATE;
import static io.github.jonarzz.lastfm2spotify.ms.spotify.auth.Scope.PLAYLIST_MODIFY_PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.util.List;

@DisplayName("Authorization service tests")
class AuthorizationServiceTest {

    private static final String HOST = "example.com";
    private static final String BASE_PATH = "/endpoint";
    private static final String BASE_URL = "http://" + HOST + BASE_PATH;
    private static final String CLIENT_ID = "abc123qwe456abc123qwe456abc123qw";

    private AuthorizationService authorizationService = new AuthorizationService(BASE_URL, CLIENT_ID);

    @Test
    @DisplayName("Get access URI")
    void getAccessUri() {
        String redirectUri = "http://test.example.com/redirect";
        String correlationId = "a1b2c3d4e5";
        AccessParams accessParams = new AccessParams();
        accessParams.setRedirectUri(redirectUri);
        accessParams.setCorrelationId(correlationId);
        accessParams.setScopes(List.of(PLAYLIST_MODIFY_PUBLIC, PLAYLIST_MODIFY_PRIVATE));

        URI result = authorizationService.getAccessUri(accessParams);

        assertThat(result)
                .as("Access URI")
                .hasHost(HOST)
                .hasNoPort()
                .hasPath(BASE_PATH)
                .hasParameter("redirect_uri",   redirectUri)
                .hasParameter("state",          correlationId)
                .hasParameter("scope",          "playlist-modify-public playlist-modify-private")
                .hasParameter("client_id",      CLIENT_ID)
                .hasParameter("response_type", "token");
    }
}