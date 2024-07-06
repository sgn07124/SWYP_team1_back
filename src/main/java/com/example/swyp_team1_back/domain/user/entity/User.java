package com.example.swyp_team1_back.domain.user.entity;

import com.example.swyp_team1_back.domain.bookmark.entity.BookmarkTip;
import com.example.swyp_team1_back.domain.cs.entity.Cs;
import com.example.swyp_team1_back.domain.tip.entity.Tip;
import com.example.swyp_team1_back.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Getter
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
    private char agree_TOS;  // 이용약관 동의(필수)

    @Column(name = "agree_PICU", nullable = false)
    private char agree_PICU;  // 개인정보 처리방침 동의(필수)

    @Column(name = "agree_MT")
    private char agree_marketing;  // 마케팅 정보 이메일 수신 동의(선택)

    private boolean fromSocial;  // // 소셜 회원가입 여부 true:소셜, false:일반

    @OneToMany(mappedBy = "user")
    private List<Tip> tips = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Cs> csList = new ArrayList<>();



    /**
     * public class User에 "implements UserDetails" 추가 후 Spring Security 인증 정보 작성하면서 시작하면 될 거 같아요!
     * application.properties 불편하시면 application.yml 형식으로 수정해서 적용해도 됩니다!
     */

}
