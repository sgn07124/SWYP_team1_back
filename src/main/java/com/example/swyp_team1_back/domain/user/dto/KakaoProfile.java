package com.example.swyp_team1_back.domain.user.dto;

import lombok.Data;

@Data
public class KakaoProfile {

    private Long id;
    private String connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Data
    public static class Properties {
        private String nickname;
    }

    @Data
    public static class KakaoAccount {
        private Boolean profile_nickname_needs_agreement;
        private Profile profile;
        private Boolean has_email;
        private Boolean email_needs_agreement;
        private Boolean is_email_valid;
        private Boolean is_email_verified;
        private String email;

        @Data
        public static class Profile {
            private String nickname;
            private Boolean is_default_nickname;
        }
    }
}
