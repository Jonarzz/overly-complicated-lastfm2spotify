package io.github.jonarzz.lastfm2spotify.commons.dto;

import lombok.Data;

@Data
public class ValidationErrorResponse {

    private String timestamp;
    private String path;
    private int status;
    private String error;
    private String message;
    private String requestId;

}
