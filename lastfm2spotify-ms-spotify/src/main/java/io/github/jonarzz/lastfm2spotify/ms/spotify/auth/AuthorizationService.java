package io.github.jonarzz.lastfm2spotify.ms.spotify.auth;

import static java.util.stream.Collectors.joining;

import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.Collection;

class AuthorizationService {

    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";
    private static final String STATE_PARAM = "state";
    private static final String SCOPE_PARAM = "scope";

    private static final String RESPONSE_TYPE_VALUE = "token";

    private String accessBaseUrl;
    private String clientId;

    AuthorizationService(String accessBaseUrl, String clientId) {
        this.accessBaseUrl = accessBaseUrl;
        this.clientId = clientId;
    }

    URI getAccessUri(String redirectUri, String correlationId, Collection<Scope> scopes) {
        // TODO testy
        return UriComponentsBuilder.fromHttpUrl(accessBaseUrl)
                                   .queryParam(CLIENT_ID_PARAM, clientId)
                                   .queryParam(REDIRECT_URI_PARAM, redirectUri)
                                   .queryParam(STATE_PARAM, correlationId)
                                   .queryParam(SCOPE_PARAM, mapScopes(scopes))
                                   .queryParam(RESPONSE_TYPE_PARAM, RESPONSE_TYPE_VALUE)
                                   .build()
                                   .toUri();
    }

    private String mapScopes(Collection<Scope> scopes) {
        return scopes.stream()
                     .map(Scope::spotifyValue)
                     .collect(joining(" "));
    }

}
