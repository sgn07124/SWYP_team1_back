package com.example.swyp_team1_back.domain.user.entity;

import com.example.swyp_team1_back.domain.cs.entity.Cs;
import com.example.swyp_team1_back.domain.tip.entity.Tip;
import com.example.swyp_team1_back.domain.user.dto.CreateUserDTO;
import com.example.swyp_team1_back.global.common.entity.BaseTimeEntity;
import com.example.swyp_team1_back.global.common.response.CustomFieldException;
import com.example.swyp_team1_back.global.common.response.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "User")
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User extends BaseTimeEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false, length = 45)
    private String phone;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;  // 권한(USER, ADMIN)

    @Column(name = "imgurl", nullable = false)
    private String imgUrl;  // 회원 프로필

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;  // 닉네임

    @Column(name = "agree_TOS", nullable = false)
    private Boolean agree_TOS;  // 이용약관 동의(필수)

    @Column(name = "agree_PICU", nullable = false)
    private Boolean agree_PICU;  // 개인정보 처리방침 동의(필수)

    @Column(name = "agree_MT")
    private Boolean agree_marketing;  // 마케팅 정보 이메일 수신 동의(선택)

    @Column(name = "from_social")
    private boolean from_social;

    @Column(name = "isDeleted")
    private boolean isDeleted = false;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Tip> tips = new ArrayList<>();  // 회원 탈퇴 시, 작성했던 팁들도 삭제

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Cs> csList = new ArrayList<>();

    public static User createUser(CreateUserDTO dto, PasswordEncoder passwordEncoder, String defaultProfileImageUrl) {
        if (!dto.getAgreeTOS()) {
            throw new CustomFieldException("agreeTOS", "필수 약관동의에 동의해주세요.", ErrorCode.AGREE_TOS_NOT_CHECKED);
        }

        if (!dto.getAgreePICU()) {
            throw new CustomFieldException("agreePICU", "필수 약관동의에 동의해주세요.", ErrorCode.AGREE_PICU_NOT_CHECKED);
        }

        if (!dto.getPassword().equals(dto.getRePassword())) {
            throw new CustomFieldException("rePassword", "비밀번호가 맞지 않습니다. 다시 입력해주세요.", ErrorCode.PASSWORD_UNMATCHED);
        }

        User user = new User();
        user.email = dto.getEmail();
        user.password = passwordEncoder.encode(dto.getPassword());
        user.name = dto.getName();
        user.phone = dto.getPhone();
        user.agree_TOS = dto.getAgreeTOS();
        user.agree_PICU = dto.getAgreePICU();
        user.agree_marketing = dto.getAgreeMarketing();

        user.role = Role.ROLE_USER;
        user.nickname = dto.getEmail();
        user.imgUrl = defaultProfileImageUrl;
        return user;
    }


}
