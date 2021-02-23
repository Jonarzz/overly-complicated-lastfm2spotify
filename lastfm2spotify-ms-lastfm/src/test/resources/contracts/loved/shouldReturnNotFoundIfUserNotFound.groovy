package contracts.loved

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should return 404 Not Found if user not found'

    request {
        method 'GET'
        url '/loved/not_existing_user'
    }

    response {
        // TODO 404 Not Found + body with message
        status INTERNAL_SERVER_ERROR()
        bodyMatchers {
            jsonPath('$', byNull())
        }
    }
}