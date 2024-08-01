package com.example.swyp_team1_back.domain.user.dto;

public class AuthResponse {
    private String jwtToken;
    private String nickname;

    // 기본 생성자
    public AuthResponse() {
    }

    // 생성자
    public AuthResponse(String jwtToken, String nickname) {
        this.jwtToken = jwtToken;
        this.nickname = nickname;
    }

    // Getters and setters
    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
