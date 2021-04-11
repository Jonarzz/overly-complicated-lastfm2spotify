package contracts.auth.acces

import org.apache.commons.lang3.RandomStringUtils
import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should return bad request when correlation ID param is too short'

    request {
        method 'GET'
        urlPath('/auth/access') {
            queryParameters {
                parameter 'redirectUri': 'http://redirect.com'
                parameter 'correlationId': RandomStringUtils.randomAlphanumeric(5)
                parameter 'scopes': 'PLAYLIST_MODIFY_PUBLIC'
            }
        }
    }

    response {
        status BAD_REQUEST()
        body([
                message: 'correlationId size must be between 10 and 50'
        ])
    }

}