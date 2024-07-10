package com.example.swyp_team1_back.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
    private String grantType; //JWT에 대한 인증 타입
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
}