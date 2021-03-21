package io.github.jonarzz.lastfm2spotify.commons.test.web;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;

import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class RestDocsConfiguration {

    private RestDocsConfiguration() {
    }

    public static <T> Consumer<EntityExchangeResult<T>> documentWithoutPrettyPrint(String identifier, String... additionalIgnoredHeaders) {
        return WebTestClientRestDocumentation.document(identifier,
                                                       preprocessRequest(headersPreprocessor(additionalIgnoredHeaders)),
                                                       preprocessResponse(headersPreprocessor(additionalIgnoredHeaders)));
    }

    public static <T> Consumer<EntityExchangeResult<T>> documentWithPrettyPrint(String identifier, String... additionalIgnoredHeaders) {
        return WebTestClientRestDocumentation.document(identifier,
                                                       preprocessRequest(prettyPrint(),
                                                                         headersPreprocessor(additionalIgnoredHeaders)),
                                                       preprocessResponse(prettyPrint(),
                                                                          headersPreprocessor(additionalIgnoredHeaders)));
    }

    private static OperationPreprocessor headersPreprocessor(String... additionalIgnoredHeaders) {
        String[] headers = Stream.concat(
                Stream.of("Accept", "Content-Length", "Host", "Vary"),
                Stream.of(additionalIgnoredHeaders)
        ).toArray(String[]::new);
        return removeHeaders(headers);
    }

}
