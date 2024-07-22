package com.example.swyp_team1_back.domain.user.service;

import com.example.swyp_team1_back.domain.user.dto.*;
import com.example.swyp_team1_back.domain.user.entity.Role;
import com.example.swyp_team1_back.domain.user.entity.User;
import com.example.swyp_team1_back.domain.user.repository.RefreshTokenRepository;
import com.example.swyp_team1_back.domain.user.repository.UserRepository;
import com.example.swyp_team1_back.global.common.response.CustomFieldException;
import com.example.swyp_team1_back.global.common.response.ErrorCode;
import com.example.swyp_team1_back.global.jwt.TokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;



import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.swyp_team1_back.global.jwt.JwtProperties.EXPIRATION_TIME;
import static com.nimbusds.oauth2.sdk.token.TokenTypeURI.JWT;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;


    private static final String DEFAULT_PROFILE_IMAGE_URL = "https://swyp-team1-s3-bucket.s3.ap-northeast-2.amazonaws.com/default_image.png";



    @Transactional
    public User signUp(CreateUserDTO signUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new CustomFieldException("email", "이미 등록된 이메일입니다.", ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (userRepository.existsByPhone(signUpRequest.getPhone())) {
            throw new CustomFieldException("phone", "이미 등록된 연락처입니다.", ErrorCode.PHONE_ALREADY_EXISTS);
        }

        User user = User.createUser(signUpRequest, passwordEncoder, DEFAULT_PROFILE_IMAGE_URL);

        return userRepository.save(user);
    }

    @Transactional
    public TokenDto login(LoginRequestDto loginRequestDto) {
        // 사용자 ID로 유저 검색
        Optional<User> userOptional = userRepository.findByEmail(loginRequestDto.getEmail());
        if (userOptional.isEmpty()) {
            throw new CustomFieldException("email", "없는 이메일입니다.", ErrorCode.EMAIL_NOT_FOUND);
        }

        User user = userOptional.get();

        // 비밀번호 확인
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new CustomFieldException("password","비밀번호가 올바르지 않습니다.", ErrorCode.INVALID_PASSWORD);
        }

        // AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // Refresh Token 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 인증 정보 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }

    public OauthToken getAccessToken(String code) {
        String KAKAO_TOKEN_REQUEST_URI = "https://kauth.kakao.com/oauth/token";

        RestTemplate restTemplate = new RestTemplate(); //통신에 유용
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "5d3ed4c53f6081a9a1503e178dfdfaeb"); // 카카오 REST API 키
        params.add("redirect_uri", "http://15.164.202.203:8080/api/user/login/kakao");
        params.add("client_secret", "SazkBcme6hi3TkyhRBBps3Hl0G7rMfcP");
        params.add("code", code);

        //http헤더와 http바디의 정보를 담기 위해 객체 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(KAKAO_TOKEN_REQUEST_URI, request, String.class);

        //String으로 받음 JSON형식 데이터를 객체로 변환
        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            OauthToken oauthToken = null;
            try {
                oauthToken = objectMapper.readValue(response.getBody(), OauthToken.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse access token response", e);
            }
            return oauthToken;
        }
        return null;
    }

    // 회원가입 시 사용자 정보를 저장하고 JWT 토큰을 생성하는 메서드
    public String saveUserAndGetToken(String token, boolean agreePicu, boolean agreeTos, boolean agreeMarketing) {
        KakaoProfile profile = findProfile(token);

        Optional<User> optionalUser = userRepository.findByEmail(profile.getKakao_account().getEmail());

        User user;
        if (optionalUser.isEmpty()) {
            user = User.builder()
                    .phone("000-0000-0000")
                    .email(profile.getKakao_account().getEmail())
                    .nickname(profile.getKakao_account().getProfile().getNickname())
                    .imgUrl(DEFAULT_PROFILE_IMAGE_URL)
                    .role(Role.ROLE_USER)
                    .agree_PICU(agreePicu)
                    .agree_TOS(agreeTos)
                    .agree_marketing(agreeMarketing)
                    .from_social(true)
                    .build();

            userRepository.save(user);
        } else {
            user = optionalUser.get();
        }

        // 권한 부여
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        // Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);

        // JWT 생성
        return tokenProvider.generateTokenDto(authentication).getAccessToken();
    }



    //access token으로 카카오 서버에서 사용자 정보가져옴
    public KakaoProfile findProfile(String token) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers);

        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoProfile;
    }

    public KakaoUserInfoDto getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication from SecurityContext: {}", authentication);

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + userDetails.getUsername()));
            return new KakaoUserInfoDto(
                    user.getId().toString(),
                    user.getEmail(),
                    user.getNickname()
            );
        } else {
            throw new RuntimeException("Principal is not an instance of UserDetails");
        }
    }


}
