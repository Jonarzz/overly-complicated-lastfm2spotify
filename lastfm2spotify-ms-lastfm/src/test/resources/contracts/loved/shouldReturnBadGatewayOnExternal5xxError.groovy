package contracts.loved

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should return 502 Bad Gateway on external API 5xx error'

    request {
        method 'GET'
        url '/loved/external_api_500_user'
    }

    response {
        // TODO 502 Bad Gateway + body with message
        status INTERNAL_SERVER_ERROR()
        bodyMatchers {
            jsonPath('$', byNull())
        }
    }
}