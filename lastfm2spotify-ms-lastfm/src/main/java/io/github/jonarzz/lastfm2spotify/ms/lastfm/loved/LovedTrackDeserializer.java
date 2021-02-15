package io.github.jonarzz.lastfm2spotify.ms.lastfm.loved;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

class LovedTrackDeserializer extends JsonDeserializer<LovedTrack> {

    @Override
    public LovedTrack deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode root = parser.getCodec().readTree(parser);
        String trackTitle = root.get("name").textValue();
        String artistName = root.get("artist").get("name").textValue();
        return new LovedTrack(artistName, trackTitle);
    }

}
