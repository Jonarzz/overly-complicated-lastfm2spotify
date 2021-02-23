package contracts.loved

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should return loved tracks for single page'

    request {
        ignored() // TODO fix web test client configuration to handle Flux properly
        method 'GET'
        url '/loved/single_loved_tracks_page_user'
    }

    response {
        status OK()
        body '''\
            [{
                artist: 'Corrosion of Conformity',
                title: 'Clean My Wounds'
            }]
        '''
    }
}