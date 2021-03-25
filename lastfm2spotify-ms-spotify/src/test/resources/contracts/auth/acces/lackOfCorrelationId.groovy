package contracts.auth.acces


import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should return bad request when no correlation ID param present'

    request {
        method 'GET'
        urlPath('/auth/access') {
            queryParameters {
                parameter 'redirectUri': 'http://redirect.com'
                parameter 'scopes': 'PLAYLIST_MODIFY_PUBLIC'
            }
        }
    }

    response {
        status BAD_REQUEST()
    }

}