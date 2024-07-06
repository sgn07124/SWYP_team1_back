package com.example.swyp_team1_back.domain.user.service;

import com.example.swyp_team1_back.domain.user.dto.CreateUserDTO;
import com.example.swyp_team1_back.domain.user.entity.User;
import com.example.swyp_team1_back.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User signUp(CreateUserDTO signUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalStateException("이미 사용 중인 이메일 입니다.");
        }

        if (userRepository.existsByPhone(signUpRequest.getPhone())) {
            throw new IllegalStateException("이미 사용 중인 전화번호 입니다.");
        }

        User user = User.createUser(signUpRequest, passwordEncoder);

        return userRepository.save(user);
    }
}
