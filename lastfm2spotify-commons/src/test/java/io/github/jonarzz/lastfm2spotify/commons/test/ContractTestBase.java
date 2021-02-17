package io.github.jonarzz.lastfm2spotify.commons.test;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
public abstract class ContractTestBase {

    @Autowired
    private WebApplicationContext applicationContext;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider, TestInfo testInfo) {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(applicationContext)
                                                  .apply(documentationConfiguration(provider))
                                                  .alwaysDo(document(getClass().getSimpleName() + "_" + testInfo.getDisplayName(),
                                                                     preprocessRequest(prettyPrint(),
                                                                                       removeHeaders("Host")),
                                                                     preprocessResponse(prettyPrint(),
                                                                                        removeHeaders("Vary", "Content-Length"))))
                                                  .build());
    }

}
