package contracts.auth.acces

import org.apache.commons.lang3.RandomStringUtils
import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should return bad request when redirect URI param is invalid'

    request {
        method 'GET'
        urlPath('/auth/access') {
            queryParameters {
                parameter 'redirectUri': 'totally not URI'
                parameter 'correlationId': RandomStringUtils.randomAlphanumeric(20)
                parameter 'scopes': 'PLAYLIST_MODIFY_PUBLIC'
            }
        }
    }

    response {
        status BAD_REQUEST()
        body([
                message: 'redirectUri must match "^https?://.+$"'
        ])
    }

}