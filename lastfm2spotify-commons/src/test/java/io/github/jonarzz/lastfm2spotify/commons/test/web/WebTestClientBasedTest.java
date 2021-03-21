package io.github.jonarzz.lastfm2spotify.commons.test.web;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
public abstract class WebTestClientBasedTest {

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider, TestInfo testInfo) {
        // document(getClass().getSimpleName() + "_" + testInfo.getDisplayName()) would be used
        // but there's no way to handle sth like 'alwaysDo' with WebTestClient
        // https://github.com/ScaCap/spring-auto-restdocs/blob/master/samples/java-webflux/src/test/java/capital/scalable/restdocs/example/testsupport/WebTestClientTestBase.java#L95
        // https://stackoverflow.com/questions/56900082/configure-webtestclient-to-always-do-something
        RestAssuredWebTestClient.applicationContextSetup(applicationContext);}

}
