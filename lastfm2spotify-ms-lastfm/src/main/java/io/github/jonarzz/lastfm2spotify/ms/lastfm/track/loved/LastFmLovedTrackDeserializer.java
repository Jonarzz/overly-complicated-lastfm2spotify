package io.github.jonarzz.lastfm2spotify.ms.lastfm.track.loved;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

class LastFmLovedTrackDeserializer extends JsonDeserializer<LastFmLovedTrack> {

    @Override
    public LastFmLovedTrack deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode root = parser.getCodec().readTree(parser);
        String trackTitle = root.get("name").textValue();
        String artistName = root.get("artist").get("name").textValue();
        return new LastFmLovedTrack(artistName, trackTitle);
    }

}
