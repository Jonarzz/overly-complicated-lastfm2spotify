package io.github.jonarzz.lastfm2spotify.ms.entrypoint.migration;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.hamcrest.text.IsEmptyString.emptyOrNullString;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mock.web.MockAsyncContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.AsyncListener;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@DisplayName("Migration events tests")
@ExtendWith(RestDocumentationExtension.class)
class MigrationEventsTest {

    private MigrationConfiguration configuration = new MigrationConfiguration();

    private MigrationEventService migrationEventService;

    private MockMvc mockMvc;
    private RestDocumentationContextProvider restDocumentationContextProvider;

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {
        restDocumentationContextProvider = provider;
        mockMvc = configureMockMvc(configuration.migrationEventEmitterCreationStrategy());
    }

    @Test
    @DisplayName("Subscriber receives the same emitter for the same LastFM username")
    void subscriberReceivesTheSameEmitterForTheSameLastFmUsername() throws Exception {
        var lastFmUsername = "username";
        var event = "event";

        MvcResult firstSubscriber = subscribeForEvents(lastFmUsername);
        MvcResult secondSubscriber = subscribeForEvents(lastFmUsername);

        migrationEventService.emit(lastFmUsername, event);
        migrationEventService.completeEmitting(lastFmUsername);

        for (MvcResult subscriber : List.of(firstSubscriber, secondSubscriber)) {
            subscriber.getAsyncResult();
            mockMvc.perform(asyncDispatch(subscriber))
                   .andExpect(content().string(containsString(event)));
        }
    }

    @Test
    @DisplayName("Subscriber receives different emitter for different LastFM username")
    void subscriberReceivesDifferentEmitterForDifferentLastFmUsername() throws Exception {
        var usernameWithEvent = "username1";
        var usernameWithoutEvent = "username2";
        var event = "event";

        MvcResult subscriberWithEvent = subscribeForEventsWithDocumentation(usernameWithEvent, "migrationStatus");
        MvcResult subscriberWithoutEvent = subscribeForEvents(usernameWithoutEvent);

        migrationEventService.emit(usernameWithEvent, event);
        migrationEventService.completeEmitting(usernameWithEvent);
        migrationEventService.completeEmitting(usernameWithoutEvent);

        subscriberWithEvent.getAsyncResult();
        subscriberWithoutEvent.getAsyncResult();
        mockMvc.perform(asyncDispatch(subscriberWithEvent))
               .andExpect(content().string(containsString(event)));
        mockMvc.perform(asyncDispatch(subscriberWithoutEvent))
               .andExpect(content().string(emptyOrNullString()));
    }

    @Test
    @DisplayName("Subscriber receives events in order")
    void subscriberReceivesMigrationEventsInOrder() throws Exception {
        var lastFmUsername = "username";
        var firstEvent = "first event";
        var secondEvent = "second event";
        var thirdEvent = "third event";

        MvcResult subscriber = subscribeForEvents(lastFmUsername);

        migrationEventService.emit(lastFmUsername, firstEvent);
        migrationEventService.emit(lastFmUsername, secondEvent);
        migrationEventService.emit(lastFmUsername, thirdEvent);
        migrationEventService.completeEmitting(lastFmUsername);

        subscriber.getAsyncResult();
        mockMvc.perform(asyncDispatch(subscriber))
               .andExpect(content().string(stringContainsInOrder(firstEvent, secondEvent, thirdEvent)));
    }

    @Test
    @DisplayName("Subscriber does not receive events in case of IO error")
    void subscriberDoesNotReceiveEventsInCaseOfIoError() throws Exception {
        SseEmitter emitter = spy(new SseEmitter());
        MockMvc mockMvc = configureMockMvc(new MockedEmitters(emitter));
        var lastFmUsername = "username";
        var errorEvent = "error event";
        doThrow(new IOException("Test error message"))
                .when(emitter)
                .send(errorEvent);

        MvcResult subscriber = subscribeForEvents(lastFmUsername);

        migrationEventService.emit(lastFmUsername, errorEvent);

        subscriber.getAsyncResult();
        mockMvc.perform(asyncDispatch(subscriber))
               .andExpect(content().string(emptyOrNullString()));
    }

    private MvcResult subscribeForEventsWithDocumentation(String lastFmUsername, String documentName) throws Exception {
        return subscribeForEvents(lastFmUsername, documentName);
    }

    private MvcResult subscribeForEvents(String lastFmUsername) throws Exception {
        return subscribeForEvents(lastFmUsername, null);
    }

    private MvcResult subscribeForEvents(String lastFmUsername, String documentName) throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/migration/{lastFmUsername}/loved/status", lastFmUsername))
                                             .andExpect(request().asyncStarted());
        if (documentName != null) {
            resultActions = resultActions.andDo(document(documentName,
                                                         preprocessRequest(prettyPrint(),
                                                                           removeHeaders("Host")),
                                                         preprocessResponse(prettyPrint(),
                                                                            removeHeaders("Vary"))));
        }
        MvcResult result = resultActions.andReturn();
        MockAsyncContext asyncContext = (MockAsyncContext) result.getRequest().getAsyncContext();
        if (asyncContext == null) {
            fail("MockMVC async context is null");
        }
        Executors.newScheduledThreadPool(2)
                 .schedule(() -> {
                     for (AsyncListener listener : asyncContext.getListeners()) {
                         try {
                             listener.onTimeout(null);
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                     }
                 }, 100, TimeUnit.MILLISECONDS);
        return result;
    }

    private MockMvc configureMockMvc(MigrationEventEmitters eventProvider) {
        migrationEventService = configuration.migrationEventService(eventProvider);
        return MockMvcBuilders.standaloneSetup(new MigrationController(null, migrationEventService))
                              .addPlaceholderValue("lastfm2spotify.web.accepted-origin-host", "localhost")
                              .apply(documentationConfiguration(restDocumentationContextProvider))
                              .build();
    }

    private static class MockedEmitters extends DefaultMigrationEventEmitters {

        private SseEmitter emitter;

        MockedEmitters(SseEmitter emitter) {
            this.emitter = emitter;
        }

        @Override
        public SseEmitter create(String lastFmUsername) {
            return emitter;
        }

        @Override
        public Collection<SseEmitter> get(String lastFmUsername) {
            return Set.of(emitter);
        }
    }

}