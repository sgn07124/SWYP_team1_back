package com.example.swyp_team1_back.domain.user.controller;

import com.example.swyp_team1_back.domain.user.dto.*;
import com.example.swyp_team1_back.domain.user.entity.User;
import com.example.swyp_team1_back.domain.user.repository.UserRepository;
import com.example.swyp_team1_back.domain.user.service.UserService;
import com.example.swyp_team1_back.global.common.response.ErrorCode;
import com.example.swyp_team1_back.global.common.response.Response;
import com.example.swyp_team1_back.global.common.response.ResponseUtil;
import com.example.swyp_team1_back.global.jwt.JwtProperties;
import com.example.swyp_team1_back.global.jwt.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@ResponseBody
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "회원가입 및 로그인 컨트롤러", description = "로그인, 회원가입 등 회원 정보 관련 API")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private HttpSession session;

    @PostMapping("/signup")
    @Operation(summary = "일반 회원가입", description = "회원은 이메일과 비밀번호로 일반 회원가입을 한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "4001", description = "Validation Error"),
            @ApiResponse(responseCode = "4002", description = "이미 등록된 이메일입니다."),
            @ApiResponse(responseCode = "4003", description = "비밀번호가 맞지 않습니다. 다시 입력해주세요.(rePassword)"),
            @ApiResponse(responseCode = "4004", description = "이미 등록된 연락처입니다."),
            @ApiResponse(responseCode = "4005", description = "필수 약관동의에 동의해주세요.(이용약관 동의)"),
            @ApiResponse(responseCode = "4006", description = "필수 약관동의에 동의해주세요.(개인정보 처리방침 동의)")
    })
    @Parameters({
            @Parameter(name = "email", description = "이메일 형식이어야 합니다.", example = "test1@naver.com"),
            @Parameter(name = "password", description = "비밀번호는 최소 8자리 이상이어야 합니다.", example = "abcd1234"),
            @Parameter(name = "rePassword", description = "비밀번호 재확인은 필수 항목입니다."),
            @Parameter(name = "name", description = "이름은 한글 또는 영문으로 입력해야 하며, 한글 자음과 모음만 입력할 수 없고, 한글과 영문을 동시에 입력할 수 없습니다."),
            @Parameter(name = "phone", description = "전화번호는 010-****-**** 형식으로 입력해야 합니다. *에는 숫자만 올 수 있습니다."),
            @Parameter(name = "agreeTOS", description = "이용약관 동의는 필수 항목입니다.", example = "true"),
            @Parameter(name = "agreePICU", description = "개인정보 처리방침 동의는 필수 항목입니다.", example = "true"),
            @Parameter(name = "agreeMarketing", description = "마케팅 이메일 수신 동의는 선택 항목입니다.", example = "true or false"),
    })

    public ResponseEntity<Response<Void>> signUp(@RequestBody @Valid CreateUserDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<Response.ErrorDetail> errors = bindingResult.getFieldErrors().stream()
                    .map(error -> {
                        Response.ErrorDetail detail = new Response.ErrorDetail();
                        detail.setField(error.getField());
                        detail.setReason(error.getDefaultMessage());
                        return detail;
                    }).collect(Collectors.toList());
            return ResponseUtil.createValidationErrorResponse("회원가입에 실패하였습니다.", errors);
        }
        userService.signUp(dto);
        return ResponseUtil.createSuccessResponseWithoutPayload("회원가입에 성공하였습니다.");
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "회원은 이메일과 비밀번호로 로그인할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @Parameters({
            @Parameter(name = "email", description = "이메일 형식이어야 합니다.", example = "test1@naver.com"),
            @Parameter(name = "password", description = "비밀번호는 최소 8자리 이상이어야 합니다.", example = "abcd1234"),
    })
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = userService.login(loginRequestDto);
        return ResponseEntity.ok(loginResponseDto);
    }


    // 카카오 로그인 엔드포인트 추가
    @GetMapping("/login/kakao")
    @Operation(summary = "카카오 로그인", description = "프론트에서 받은 인가 코드로 카카오 액세스 토큰을 발급받는다.")
    public ResponseEntity getLogin(@RequestParam("code") String code, boolean agreePicu, boolean agreeTos, boolean agreeMarketing) {

        try {
            // 인가 코드로 카카오 액세스 토큰을 발급받는다.
            OauthToken oauthToken = userService.getAccessToken(code);
            logger.info("OauthToken: " + oauthToken);

            // 카카오 회원정보 디비 저장후 jwt생성
            String result = userService.saveUserAndGetToken(oauthToken.getAccess_token());
            //logger.info("JWT Token: " + jwtToken);

            if (result.startsWith("http")) {
                // 리다이렉트 URL인 경우
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(URI.create(result));
                return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
            } else {
                // JWT 토큰인 경우
                HttpHeaders headers = new HttpHeaders();
                headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + result);
                logger.info("JWT Token: " + result);

                // 리다이렉트 URL 설정
                HttpHeaders redirectHeaders = new HttpHeaders();
                redirectHeaders.setLocation(URI.create("https://swyg-front.vercel.app/my/doing"));
                return new ResponseEntity<>(redirectHeaders, HttpStatus.SEE_OTHER);
            }
        } catch (HttpClientErrorException e) {
            // 로그 추가
            logger.error("HttpClientErrorException: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid authorization code");
        } catch (Exception e) {
            // 기타 예외 처리
            logger.error("Exception: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PostMapping("/join/kakao")
    public ResponseEntity<?> joinUser(@RequestBody CreateUserDTO userDto) {
        try {
            userService.saveUser(userDto);
            // 회원가입 후 JWT 토큰 생성
            List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDto.getEmail(), null, authorities);

            // JWT 생성
            String jwtToken = tokenProvider.generateTokenDto(authentication).getAccessToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("https://swyg-front.vercel.app/my/doing"));
            headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        } catch (Exception e) {
            logger.error("Exception in joinUser: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User registration failed");
        }
    }


    @GetMapping("/details/pw")
    @ApiResponses({
            @ApiResponse(responseCode = "4001", description = "Validation Error"),
    })
    @Parameters({
            @Parameter(name = "email", description = "이메일 형식이어야 합니다.", example = "test1@naver.com"),
            @Parameter(name = "name", description = "이름은 한글 또는 영문으로 입력해야 하며, 한글 자음과 모음만 입력할 수 없고, 한글과 영문을 동시에 입력할 수 없습니다."),
            @Parameter(name = "phone", description = "전화번호는 010-****-**** 형식으로 입력해야 합니다. *에는 숫자만 올 수 있습니다.")
    })
    @Operation(summary = "비밀번호 재설정", description = "회원은 비밀번호를 재설정하기위해 이메일, 이름, 전화번호로 본인인증을 해야한다.")
    public ResponseEntity<Response<Void>> verifyUser(@Valid @RequestBody PasswordResetRequestDto requestDto) {
        try {
            boolean isRegistered = userService.verifyUser(requestDto.getEmail(), requestDto.getName(), requestDto.getPhone());
            if (!isRegistered) {
                throw new IllegalArgumentException("유효하지 않은 사용자입니다.");
            }
            session.setAttribute("verifiedEmail", requestDto.getEmail());
            return ResponseUtil.createSuccessResponseWithoutPayload("유효한 사용자입니다.");
        } catch (Exception e) {
            return ResponseUtil.createExceptionResponse("유효하지 않은 사용자입니다.", ErrorCode.VALIDATION_ERROR, e.getMessage());
        }

    }


    @PatchMapping("/details/repw")
    @Operation(summary = "비밀번호 재설정", description = "인증된 사용자는 이 엔드포인트를 통해 비밀번호를 재설정할 수 있다.")
    @Parameters({
            @Parameter(name = "password", description = "비밀번호는 최소 8자리 이상이어야 합니다.", example = "abcd1234"),
            @Parameter(name = "rePassword", description = "비밀번호 재확인은 필수 항목입니다."),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 재설정 성공"),
            @ApiResponse(responseCode = "4003", description = "비밀번호가 맞지 않습니다. 다시 입력해주세요.(rePassword)")
    })
    public ResponseEntity<Response<Void>> resetPassword(@Valid @RequestBody PasswordChangeRequestDto requestDto, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseUtil.createExceptionResponse("본인 인증이 필요합니다.", ErrorCode.UNAUTHORIZED, "세션이 존재하지 않습니다.");
        }

        String email = (String) session.getAttribute("verifiedEmail");
        if (email == null) {
            return ResponseUtil.createExceptionResponse("Verification is required.", ErrorCode.UNAUTHORIZED, "검증된 이메일이 세션에 없습니다.");
        }


        boolean isPasswordChanged = userService.changePassword(email, requestDto.getPassword(), requestDto.getRepassword());
        if (!isPasswordChanged) {
            return ResponseUtil.createExceptionResponse("Password reset failed.", ErrorCode.VALIDATION_ERROR, "비밀번호 변경 실패");
        }
        session.removeAttribute("verifiedEmail");
        return ResponseUtil.createSuccessResponseWithoutPayload("Password has been successfully reset.");
    }



}
