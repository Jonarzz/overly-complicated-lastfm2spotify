package io.github.jonarzz.lastfm2spotify.ms.lastfm.loved;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

class LovedTracksApiResponseDeserializer extends JsonDeserializer<LovedTracksApiResponse> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public LovedTracksApiResponse deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode root = parser.getCodec().readTree(parser);
        JsonNode nestedRoot = root.get("lovedtracks");
        PagingMetadata pagingMetadata = parse(nestedRoot, "@attr", new TypeReference<>() {});
        List<LovedTrack> lovedTracks = parse(nestedRoot, "track", new TypeReference<>() {});
        return new LovedTracksApiResponse(pagingMetadata, lovedTracks);
    }

    private <T> T parse(JsonNode root, String name, TypeReference<T> typeReference) throws JsonProcessingException {
        return objectMapper.readValue(root.get(name).toString(), typeReference);
    }

}
