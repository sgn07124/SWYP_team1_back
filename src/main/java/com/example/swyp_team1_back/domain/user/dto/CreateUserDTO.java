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
    @Email(message = "올바르지 않는 이메일 주소입니다.")
    private String email;

    @NotNull(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, message = "비밀번호는 숫자와 영문을 조합하여 8자 이상으로 적어주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "비밀번호는 숫자와 영문을 조합하여 8자 이상으로 적어주세요.")
    private String password;

    @NotNull(message = "비밀번호가 맞지 않습니다. 다시 입력히주세요.")
    private String rePassword;

    @NotNull(message = "이름을 잘못 작성하였습니다. 다시 입력해주세요.")
    @Pattern(regexp = "^[가-힣]{1,30}$|^[a-zA-Z]{1,30}$", message = "이름을 잘못 작성하였습니다. 다시 입력해주세요.")
    private String name;

    @NotNull(message = "올바르지 않는 연락처입니다. 숫자로만 다시 입력해주세요.")
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "올바르지 않는 연락처입니다. 숫자로만 다시 입력해주세요.")
    private String phone;

    @NotNull(message = "이용약관 동의는 필수 항목입니다.")
    private Boolean agreeTOS;

    @NotNull(message = "개인정보 처리방침 동의는 필수 항목입니다.")
    private Boolean agreePICU;

    private Boolean agreeMarketing;

}
