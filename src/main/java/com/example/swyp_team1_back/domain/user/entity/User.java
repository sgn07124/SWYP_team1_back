package com.example.swyp_team1_back.domain.user.entity;

import com.example.swyp_team1_back.domain.bookmark.entity.BookmarkTip;
import com.example.swyp_team1_back.domain.cs.entity.Cs;
import com.example.swyp_team1_back.domain.tip.entity.Tip;
import com.example.swyp_team1_back.domain.user.dto.CreateUserDTO;
import com.example.swyp_team1_back.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "User")
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

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
    private boolean agree_TOS;  // 이용약관 동의(필수)

    @Column(name = "agree_PICU", nullable = false)
    private boolean agree_PICU;  // 개인정보 처리방침 동의(필수)

    @Column(name = "agree_MT")
    private boolean agree_marketing;  // 마케팅 정보 이메일 수신 동의(선택)

    private boolean fromSocial;  // // 소셜 회원가입 여부 true:소셜, false:일반

    @OneToMany(mappedBy = "user")
    private List<Tip> tips = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Cs> csList = new ArrayList<>();

    public static User createUser(CreateUserDTO dto, PasswordEncoder passwordEncoder, String defaultProfileImageUrl) {
        if (!dto.isAgreeTOS() || !dto.isAgreePICU()) {
            throw new IllegalArgumentException("이용약관 및 개인정보 처리방침 동의는 필수");
        }

        if (!dto.getPassword().equals(dto.getRePassword())) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 재확인이 일치하지 않음");
        }

        User user = new User();
        user.email = dto.getEmail();
        user.password = passwordEncoder.encode(dto.getPassword());
        user.phone = dto.getPhone();
        user.agree_TOS = dto.isAgreeTOS();
        user.agree_PICU = dto.isAgreePICU();
        user.agree_marketing = dto.isAgreeMarketing();

        user.role = Role.USER;
        user.nickname = dto.getEmail();
        user.imgUrl = defaultProfileImageUrl;
        user.fromSocial = false;
        return user;
    }


}
