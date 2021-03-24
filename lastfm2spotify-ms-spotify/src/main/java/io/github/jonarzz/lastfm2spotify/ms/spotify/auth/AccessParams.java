package io.github.jonarzz.lastfm2spotify.ms.spotify.auth;

import com.google.common.collect.EvictingQueue;
import io.github.jonarzz.lastfm2spotify.commons.validation.ValidationPatterns;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import javax.annotation.concurrent.GuardedBy;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Queue;

@Data
@Validated
class AccessParams {

    @GuardedBy("this")
    private static final Queue<String> LAST_CORRELATION_IDS = EvictingQueue.create(10);

    @Pattern(regexp = ValidationPatterns.URL)
    private String redirectUri;
    @Pattern(regexp = ValidationPatterns.AT_LEAST_10_CHARS)
    private String correlationId;
    @Size(min = 1, max = 20)
    private Collection<Scope> scopes;

    public synchronized void setCorrelationId(String correlationId) {
        if (LAST_CORRELATION_IDS.contains(correlationId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Correlation ID has to be unique");
        }
        LAST_CORRELATION_IDS.add(correlationId);
        this.correlationId = correlationId;
    }

}

