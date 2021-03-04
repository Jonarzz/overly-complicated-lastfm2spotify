package contracts.loved

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should return 404 Not Found if user not found'

    request {
        method 'GET'
        url '/tracks/loved/not_existing_user'
    }

    response {
        status NOT_FOUND()
        body([
                errorMessage: "User with name not_existing_user not found"
        ])
    }
}