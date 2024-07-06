package com.example.swyp_team1_back.domain.user.controller;

import com.example.swyp_team1_back.domain.user.dto.CreateUserDTO;
import com.example.swyp_team1_back.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "회원가입 및 로그인 컨트롤러", description = "로그인, 회원가입 등 회원 정보 관련 API")
public class UserController {

    private static UserService userService;

    @Autowired
    public UserController(UserService userService) {
        UserController.userService = userService;
    }

    @PostMapping("/signup")
    @Operation(summary = "일반 회원가입", description = "회원은 이메일로 일반 회원가입을 한다.")
    public ResponseEntity<String> signUp(@RequestBody @Valid CreateUserDTO dto) {
        userService.signUp(dto);
        return ResponseEntity.ok("회원가입 성공!");
    }

}
