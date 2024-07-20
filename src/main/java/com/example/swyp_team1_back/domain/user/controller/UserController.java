package com.example.swyp_team1_back.domain.user.controller;

import com.example.swyp_team1_back.domain.user.dto.*;
import com.example.swyp_team1_back.domain.user.entity.User;
import com.example.swyp_team1_back.domain.user.repository.UserRepository;
import com.example.swyp_team1_back.domain.user.service.UserService;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
        );
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        return ResponseEntity.ok(tokenDto);
    }


    // 카카오 로그인 엔드포인트 추가
    @GetMapping("/login/kakao")
    @Operation(summary = "카카오 로그인", description = "프론트에서 받은 인가 코드로 카카오 액세스 토큰을 발급받는다.")
    public ResponseEntity getLogin(@RequestParam("code") String code,@RequestParam("agreePicu") boolean agreePicu,
                         @RequestParam("agreeTos") boolean agreeTos,
                         @RequestParam("agreeMarketing") boolean agreeMarketing) { //(4)

        // 인가 코드로 카카오 액세스 토큰을 발급받는다.
        OauthToken  oauthToken = userService.getAccessToken(code);

        //카카오 회원정보 디비 저장후 jwt생성
        String jwtToken = userService.saveUserAndGetToken(oauthToken.getAccess_token(), agreePicu, agreeTos, agreeMarketing);

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        return ResponseEntity.ok().headers(headers).body("카카오 로그인 success");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        KakaoUserInfoDto kakaoUserInfoDto = userService.getUser();
        return ResponseEntity.ok(kakaoUserInfoDto);
    }
}
