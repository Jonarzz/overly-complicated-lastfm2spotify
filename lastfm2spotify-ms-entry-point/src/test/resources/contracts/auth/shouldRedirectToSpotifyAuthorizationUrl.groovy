package contracts.auth

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should redirect to Spotify authorization URL'

    request {
        method 'GET'
        url '/auth/spotify'
    }

    response {
        status FOUND()
        headers {
            header 'Location': 'https://accounts.spotify.com/pl/authorize?client_id=123' // TODO
        }
    }
}