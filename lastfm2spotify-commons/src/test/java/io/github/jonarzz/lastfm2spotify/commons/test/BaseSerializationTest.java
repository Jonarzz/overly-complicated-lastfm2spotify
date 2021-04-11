package io.github.jonarzz.lastfm2spotify.commons.test;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Execution(ExecutionMode.CONCURRENT)
public abstract class BaseSerializationTest<T> {

    private ObjectMapper objectMapper = new ObjectMapper();

    private Class<T> classUnderTest;
    private CollectionType collectionType;

    protected BaseSerializationTest(Class<T> classUnderTest) {
        if (jsonToExpectedObject().size() < 1) {
            throw new IllegalStateException("jsonToExpectedObject method has to return at least 1 pair");
        }
        this.classUnderTest = classUnderTest;
        collectionType = TypeFactory.defaultInstance()
                                    .constructCollectionType(List.class, classUnderTest);
    }

    protected abstract Map<String, T> jsonToExpectedObject();

    @SuppressWarnings("unused") // used in DisabledIf annotation
    protected boolean shouldOnlyTestDeserialization() {
        return false;
    }

    @Test
    @DisplayName("Serialize single element")
    @DisabledIf(value = "shouldOnlyTestDeserialization", disabledReason = "Only deserialization should be tested for this class")
    void serializeSingleElement() throws JsonProcessingException {
        Map.Entry<String, T> jsonToObject = jsonToExpectedObject().entrySet()
                                                                  .iterator()
                                                                  .next();
        String expectedJson = jsonToObject.getKey();
        T object = jsonToObject.getValue();

        String result = objectMapper.writeValueAsString(object);

        assertThat(result)
                .as("Serialized object: " + object)
                .isEqualToIgnoringWhitespace(expectedJson);
    }

    @Test
    @DisplayName("Serialize collection")
    @DisabledIf(value = "shouldOnlyTestDeserialization", disabledReason = "Only deserialization should be tested for this class")
    void serializeCollection() throws JsonProcessingException {
        Collection<T> collection = jsonToExpectedObject().values();

        String result = objectMapper.writeValueAsString(collection);

        assertThat(result)
                .as("Serialized object: " + collection)
                .isEqualToIgnoringWhitespace(jsonToExpectedObject().keySet()
                                                                   .stream()
                                                                   .collect(joining(", ", "[", "]")));
    }

    @Test
    @DisplayName("Deserialize single element")
    void deserializeSingleElement() throws JsonProcessingException {
        Map.Entry<String, T> jsonToExpectedObject = jsonToExpectedObject().entrySet()
                                                                          .iterator()
                                                                          .next();
        String json = jsonToExpectedObject.getKey();
        T expectedObject = jsonToExpectedObject.getValue();

        T result = objectMapper.readValue(json, classUnderTest);

        assertThat(result)
                .as("Deserialized JSON: " + json)
                .isEqualTo(expectedObject);
    }

    @Test
    @DisplayName("Deserialize collection")
    void deserializeCollection() throws JsonProcessingException {
        String json = jsonToExpectedObject().keySet()
                                            .stream()
                                            .collect(joining(", ", "[", "]"));

        List<T> result = objectMapper.readValue(json, collectionType);

        assertThat(result)
                .as("Deserialized JSON: " + json)
                .containsExactlyElementsOf(jsonToExpectedObject().values());
    }

}
