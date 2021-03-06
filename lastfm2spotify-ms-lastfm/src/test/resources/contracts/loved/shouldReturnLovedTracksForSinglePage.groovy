package contracts.loved

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should return loved tracks for single page'

    request {
        method 'GET'
        url '/tracks/loved/single_loved_tracks_page_user'
    }

    response {
        status OK()
        headers {
            contentType('application/x-ndjson')
        }
        body([
                artist: 'Corrosion of Conformity',
                title : 'Clean My Wounds'
        ])
    }

}