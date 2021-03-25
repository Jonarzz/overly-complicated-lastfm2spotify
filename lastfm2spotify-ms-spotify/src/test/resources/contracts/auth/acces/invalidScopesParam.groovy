package contracts.auth.acces

import org.apache.commons.lang3.RandomStringUtils
import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should return bad request when scopes param is invalid'

    request {
        method 'GET'
        urlPath('/auth/access') {
            queryParameters {
                parameter 'redirectUri': 'http://redirect.com'
                parameter 'correlationId': RandomStringUtils.randomAlphanumeric(20)
                parameter 'scopes': 'not a valid scope'
            }
        }
    }

    response {
        status BAD_REQUEST()
    }

}