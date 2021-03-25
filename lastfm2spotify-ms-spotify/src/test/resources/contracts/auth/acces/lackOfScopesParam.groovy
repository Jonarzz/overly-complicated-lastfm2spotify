package contracts.auth.acces

import org.apache.commons.lang3.RandomStringUtils
import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should return bad request when no scopes param present'

    request {
        method 'GET'
        urlPath('/auth/access') {
            queryParameters {
                parameter 'redirectUri': 'http://redirect.com'
                parameter 'correlationId': RandomStringUtils.randomAlphanumeric(20)
            }
        }
    }

    response {
        status BAD_REQUEST()
    }

}