package com.example.swyp_team1_back.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequestDto {

    @NotNull(message = "이메일은 필수 항목입니다.")
    @Email(message = "올바르지 않는 이메일 주소입니다.")
    private String email;

    @NotNull(message = "이름은 필수 입력 값입니다.")
    @Pattern(regexp = "^[가-힣]{1,30}$|^[a-zA-Z]{1,30}$", message = "이름을 잘못 작성하였습니다.")
    private String name;

    @NotNull(message = "연락처는 필수 입력 값입니다.")
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "올바르지 않은 연락처입니다.")
    private String phone;
}
