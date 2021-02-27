package io.github.jonarzz.lastfm2spotify.commons.test;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;

import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.function.Consumer;

public class RestDocsConfiguration {

    private RestDocsConfiguration() {
    }

    public static <T> Consumer<EntityExchangeResult<T>> document(String identifier) {
        return WebTestClientRestDocumentation.document(identifier,
                                                       preprocessRequest(requestPreprocessors()),
                                                       preprocessResponse(responsePreprocessors()));
    }

    static OperationPreprocessor[] requestPreprocessors() {
        return new OperationPreprocessor[] {
                prettyPrint(),
                removeHeaders("Host", "Content-Length")
        };
    }

    static OperationPreprocessor[] responsePreprocessors() {
        return new OperationPreprocessor[] {
                prettyPrint(),
                removeHeaders("Vary", "Content-Length")
        };
    }

}
