package io.github.jonarzz.lastfm2spotify.ms.lastfm.loved;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

class LovedTracksApiResponseDeserializer extends JsonDeserializer<LovedTracksApiResponse> {

    @Override
    public LovedTracksApiResponse deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        ObjectMapper objectMapper = (ObjectMapper) parser.getCodec();
        TreeNode nestedRoot = objectMapper.readTree(parser)
                                          .get("lovedtracks");
        PagingMetadata pagingMetadata = objectMapper.treeToValue(nestedRoot.get("@attr"), PagingMetadata.class);
        List<LovedTrack> lovedTracks = objectMapper.readValue(nestedRoot.get("track").toString(), new TypeReference<>() {});
        return new LovedTracksApiResponse(pagingMetadata, lovedTracks);
    }

}
