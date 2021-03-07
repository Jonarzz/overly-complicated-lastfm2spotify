package io.github.jonarzz.lastfm2spotify.commons.test;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public abstract class BaseDeserializerTest<T> {

    private ObjectMapper objectMapper = new ObjectMapper();
    
    private Class<T> deserializedClass;
    private CollectionType collectionType;

    protected BaseDeserializerTest(Class<T> deserializedClass) {
        if (jsonToExpectedObject().size() < 1) {
            throw new IllegalStateException("jsonToExpectedObject method has to return at least 1 pair");
        }
        this.deserializedClass = deserializedClass;
        collectionType = TypeFactory.defaultInstance()
                                    .constructCollectionType(List.class, deserializedClass);
    }

    protected abstract Map<String, T> jsonToExpectedObject();

    @Test
    @DisplayName("Deserialize single element")
    void deserializeSingleElement() throws JsonProcessingException {
        Map.Entry<String, T> jsonToExpectedObject = jsonToExpectedObject().entrySet()
                                                                          .iterator()
                                                                          .next();
        String json = jsonToExpectedObject.getKey();
        T expectedObject = jsonToExpectedObject.getValue();

        T result = objectMapper.readValue(json, deserializedClass);

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
