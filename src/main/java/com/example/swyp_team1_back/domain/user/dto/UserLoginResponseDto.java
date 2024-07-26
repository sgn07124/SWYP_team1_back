package com.example.swyp_team1_back.domain.user.dto;

        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDto {
    private TokenDto token;
    private KakaoUserInfoDto userInfo;
}
