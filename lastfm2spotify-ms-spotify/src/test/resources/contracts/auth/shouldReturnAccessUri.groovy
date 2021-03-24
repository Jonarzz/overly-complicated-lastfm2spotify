package contracts.auth

import org.apache.commons.lang3.RandomStringUtils
import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should return authorization access URI'

    request {
        method 'GET'
        urlPath('/auth/access') {
            queryParameters {
                parameter 'redirectUri': 'http://redirect.com'
                parameter 'correlationId': RandomStringUtils.randomAlphanumeric(20)
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