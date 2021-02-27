package io.github.jonarzz.lastfm2spotify.commons.test;

import static io.github.jonarzz.lastfm2spotify.commons.test.RestDocsConfiguration.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.main.web-application-type=reactive"
)
@AutoConfigureRestDocs
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
public abstract class WebTestClientContractTestBase {

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider, TestInfo testInfo) {
        RestAssuredWebTestClient.applicationContextSetup(applicationContext,
                                                         documentationConfiguration(provider),
                                                         document(getClass().getSimpleName() + "_" + testInfo.getDisplayName())); // TODO fixme - not generating snippets
    }

}
