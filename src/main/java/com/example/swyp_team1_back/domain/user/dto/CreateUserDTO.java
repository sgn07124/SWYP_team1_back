package com.example.swyp_team1_back.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateUserDTO {

    @NotNull(message = "이메일은 필수 항목입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotNull(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자리 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "비밀번호는 영문과 숫자를 포함해야 합니다.")
    private String password;

    @NotNull(message = "비밀번호 재확인은 필수 항목입니다.")
    private String rePassword;

    @NotNull(message = "이름은 필수 항목입니다.")
    @Pattern(regexp = "^[가-힣]{1,30}$|^[a-zA-Z]{1,30}$", message = "이름은 한글 또는 영문으로 입력해야 하며, 한글 자음과 모음만 입력할 수 없고, 한글과 영문을 동시에 입력할 수 없습니다.")
    private String name;

    @NotNull(message = "전화번호는 필수 항목입니다.")
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호는 010-****-**** 형식으로 입력해야 합니다.")
    private String phone;

    @NotNull(message = "이용약관 동의는 필수 항목입니다.")
    private boolean agreeTOS;

    @NotNull(message = "개인정보 처리방침 동의는 필수 항목입니다.")
    private boolean agreePICU;

    private boolean agreeMarketing;

}
