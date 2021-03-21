package io.github.jonarzz.lastfm2spotify.ms.entrypoint.testutil;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@AutoConfigureRestDocs
@TestPropertySource(properties = {
        "lastfm2spotify.web.accepted-origin-host=localhost",
        "lastfm2spotify.integration.ms.lastfm.base-url=http://localhost/",
        "lastfm2spotify.integration.ms.spotify.base-url=http://localhost/"
})
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
public @interface DocumentedControllerTest {

}
