package com.example.swyp_team1_back.domain.user.dto;

import lombok.Data;

@Data
public class OauthToken {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private int refresh_token_expires_in;

    @Override
    public String toString() {
        return "OauthToken{" +
                "access_token='" + access_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", expires_in=" + expires_in +
                ", scope='" + scope + '\'' +
                ", refresh_token_expires_in=" + refresh_token_expires_in +
                '}';
    }
}
