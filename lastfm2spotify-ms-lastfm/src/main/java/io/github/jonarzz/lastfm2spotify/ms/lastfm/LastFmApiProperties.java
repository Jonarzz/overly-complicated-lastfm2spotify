package io.github.jonarzz.lastfm2spotify.ms.lastfm;

import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;

@Validated
@ConfigurationProperties(prefix = "lastfm2spotify.api.lastfm")
public class LastFmApiProperties {

    @Pattern(regexp = "^https?://.+$")
    private String baseUrl;
    @Pattern(regexp = "[a-z0-9]{32}")
    private String apiKey;
    @Range(min = 1, max = 1000)
    private int singlePageLimit = 50;

    public String baseUrl() {
        return baseUrl;
    }

    public String apiKey() {
        return apiKey;
    }

    public int singlePageLimit() {
        return singlePageLimit;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setSinglePageLimit(int singlePageLimit) {
        this.singlePageLimit = singlePageLimit;
    }

}
