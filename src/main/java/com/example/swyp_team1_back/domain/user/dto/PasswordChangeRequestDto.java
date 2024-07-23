package com.example.swyp_team1_back.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeRequestDto {

    @NotNull(message = "이메일은 필수 항목입니다.")
    private String email;
    @NotNull(message = "새 비밀번호는 필수 항목입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 숫자와 영문을 조합하여 8자 이상으로 적어주세요.")
    private String newPassword;

    @NotNull(message = "비밀번호 확인은 필수 항목입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 숫자와 영문을 조합하여 8자 이상으로 적어주세요.")
    private String rePassword;
}
