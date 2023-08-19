package com.hodolog.api.response;

import lombok.Getter;

@Getter
public class SessionResponse {

    private final String accessToken;

    public SessionResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
