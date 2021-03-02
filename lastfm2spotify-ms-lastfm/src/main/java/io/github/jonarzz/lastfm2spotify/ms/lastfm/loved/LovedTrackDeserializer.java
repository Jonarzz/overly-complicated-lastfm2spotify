package io.github.jonarzz.lastfm2spotify.ms.lastfm.loved;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Optional;

class LovedTrackDeserializer extends JsonDeserializer<LovedTrack> {

    @Override
    public LovedTrack deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode root = parser.getCodec().readTree(parser);
        return tryParseFromInternalModel(root) // TODO split to 2 DTOs and deserializers (from LastFM + internal)
                .orElseGet(() -> parseFromLastFmApi(root));
    }

    private static Optional<LovedTrack> tryParseFromInternalModel(JsonNode root) {
        return Optional.ofNullable(root.get("title"))
                       .map(JsonNode::textValue)
                       .flatMap(trackTitle -> Optional.ofNullable(root.get("artist"))
                                                      .map(JsonNode::textValue)
                                                      .map(artistName -> new LovedTrack(artistName, trackTitle)));
    }

    private static LovedTrack parseFromLastFmApi(JsonNode root) {
        String trackTitle = root.get("name").textValue();
        String artistName = root.get("artist").get("name").textValue();
        return new LovedTrack(artistName, trackTitle);
    }

}
