package com.example.swyp_team1_back.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

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
    private String role;  // 권한(USER, ADMIN)



    /**
     * public class User에 "implements UserDetails" 추가 후 Spring Security 인증 정보 작성하면서 시작하면 될 거 같아요!
     * application.properties 불편하시면 application.yml 형식으로 수정해서 적용해도 됩니다!
     */

}
