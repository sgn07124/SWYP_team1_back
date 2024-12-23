package com.example.swyp_team1_back.global.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

//스프링시큐리티에서 현재 인증된 사용자의 id가져옴
public class SecurityUtil {

    private SecurityUtil() {
    }

    // SecurityContext 에 유저 정보가 저장되는 시점
    // Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장
    public static Long getCurrentMemberId() {
        // SecurityContext 의 Authentication 객체를 이용해 회원 정보를 가져온다.
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 인증 정보가 없는 경우
        if (authentication == null || authentication.getName() == null) {
            throw  new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }

        return Long.parseLong(authentication.getName());
    }
}