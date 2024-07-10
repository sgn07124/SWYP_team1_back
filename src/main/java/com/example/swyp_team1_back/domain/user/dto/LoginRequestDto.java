package com.example.swyp_team1_back.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    @NotNull(message = "이메일은 필수 항목입니다.")
    @Email(message = "올바르지 않는 이메일 주소입니다.")
    private String email;

    @NotNull(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, message = "비밀번호는 숫자와 영문을 조합하여 8자 이상으로 적어주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "비밀번호는 숫자와 영문을 조합하여 8자 이상으로 적어주세요.")
    private String password;
}