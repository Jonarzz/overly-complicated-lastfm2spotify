package contracts.auth.acces


import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should return authorization access URI'

    request {
        method 'GET'
        urlPath('/auth/access') {
            queryParameters {
                parameter 'redirectUri': 'http://redirect.com'
                parameter 'correlationId': 'qwerty12345'
                parameter 'scopes': 'PLAYLIST_MODIFY_PUBLIC,PLAYLIST_MODIFY_PRIVATE'
            }
        }
    }

    response {
        status OK()
        bodyMatchers {
            jsonPath('$', byRegex('^https://.+$'))
        }
    }

}