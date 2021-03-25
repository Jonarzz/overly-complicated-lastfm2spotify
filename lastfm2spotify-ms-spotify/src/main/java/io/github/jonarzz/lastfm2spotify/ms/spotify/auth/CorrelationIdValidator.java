package io.github.jonarzz.lastfm2spotify.ms.spotify.auth;

import com.google.common.collect.EvictingQueue;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import javax.annotation.concurrent.GuardedBy;
import java.util.Queue;

class CorrelationIdValidator implements Validator {

    private static final String NOT_UNIQUE_CORRELATION_ID_ERROR_CODE = "lastfm2spotify.correlationIdNotUnique.message";
    private static final String NOT_UNIQUE_CORRELATION_ID_DEFAULT_MESSAGE = "Correlation ID should be unique";

    @GuardedBy("itself")
    private static final Queue<String> LAST_CORRELATION_IDS = EvictingQueue.create(10);

    @Override
    public boolean supports(Class<?> clazz) {
        return AccessParams.class == clazz;
    }

    @Override
    public void validate(Object object, Errors errors) {
        AccessParams params = (AccessParams) object;
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
