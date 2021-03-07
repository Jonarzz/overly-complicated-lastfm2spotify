package io.github.jonarzz.lastfm2spotify.commons.error;

import io.github.jonarzz.lastfm2spotify.commons.test.BaseSerializationTest;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;

@DisplayName("Error response serialization tests")
class ErrorResponseSerializationTest extends BaseSerializationTest<ErrorResponse> {

    protected ErrorResponseSerializationTest() {
        super(ErrorResponse.class);
    }

    @Override
    protected Map<String, ErrorResponse> jsonToExpectedObject() {
        return Map.of(
                "{\"errorMessage\":\"error message\"}", new ErrorResponse("error message")
        );
    }

}