package com.example.swyp_team1_back.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KakaoUserInfoDto {
    private String id;       // 카카오 고유 ID
    private String email;    // 사용자 이메일
    private String nickname; // 사용자 닉네임
}
