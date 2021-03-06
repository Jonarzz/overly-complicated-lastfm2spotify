package contracts.loved

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should return empty if no loved tracks found'

    request {
        method 'GET'
        url '/tracks/loved/no_loved_tracks_user'
    }

    response {
        status NO_CONTENT()
        body("")
    }

}