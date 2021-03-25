package contracts.auth.acces

import org.apache.commons.lang3.RandomStringUtils
import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should return bad request when no redirect URI param present'

    request {
        method 'GET'
        urlPath('/auth/access') {
            queryParameters {
                parameter 'correlationId': RandomStringUtils.randomAlphanumeric(20)
                parameter 'scopes': 'PLAYLIST_MODIFY_PUBLIC'
            }
        }
    }

    response {
        status BAD_REQUEST()
    }

}