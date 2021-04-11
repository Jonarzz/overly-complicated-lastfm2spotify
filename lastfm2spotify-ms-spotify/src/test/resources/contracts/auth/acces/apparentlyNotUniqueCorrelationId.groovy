package contracts.auth.acces


import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should return bad request when the same correlation ID is repeatedly sent'

    priority 2 // has to be executed after OK contract (same correlation ID value)

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
        status BAD_REQUEST()
        body([
            message: 'correlationId should be unique'
        ])
    }

}