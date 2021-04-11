package io.github.jonarzz.lastfm2spotify.ms.spotify.auth;

import com.google.common.collect.EvictingQueue;
import io.github.jonarzz.lastfm2spotify.commons.validation.CustomValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import javax.annotation.concurrent.GuardedBy;
import java.util.Queue;

class AccessParamsCustomValidators {

    private AccessParamsCustomValidators() {
    }

    static Validator[] all() {
        return new Validator[] {
                new CorrelationIdValidator()
        };
    }

    private static abstract class AccessParamsValidator extends CustomValidator<AccessParams> {

        AccessParamsValidator() {
            super(AccessParams.class);
        }

    }

    private static class CorrelationIdValidator extends AccessParamsValidator {

        private static final String NOT_UNIQUE_CORRELATION_ID_ERROR_CODE = "lastfm2spotify.notUnique.message";
        private static final String NOT_UNIQUE_CORRELATION_ID_DEFAULT_MESSAGE = "should be unique";

        @GuardedBy("itself")
        private static final Queue<String> LAST_CORRELATION_IDS = EvictingQueue.create(10);

        @Override
        public void executeValidation(AccessParams params, Errors errors) {
            String correlationId = params.getCorrelationId();
            if (correlationId == null) {
                return;
            }
            synchronized (LAST_CORRELATION_IDS) {
                if (LAST_CORRELATION_IDS.contains(correlationId)) {
                    errors.rejectValue("correlationId", NOT_UNIQUE_CORRELATION_ID_ERROR_CODE, NOT_UNIQUE_CORRELATION_ID_DEFAULT_MESSAGE);
                } else {
                    LAST_CORRELATION_IDS.add(correlationId);
                }
            }
        }

    }

}
