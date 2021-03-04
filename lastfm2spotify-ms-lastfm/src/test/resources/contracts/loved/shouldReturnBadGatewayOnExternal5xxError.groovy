package contracts.loved

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should return 502 Bad Gateway on external API 5xx error'

    request {
        method 'GET'
        url '/tracks/loved/external_api_500_user'
    }

    response {
        status BAD_GATEWAY()
        body([
                errorMessage: "LastFM API is unavailable at the moment"
        ])
    }
}