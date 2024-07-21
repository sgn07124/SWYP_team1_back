package com.example.swyp_team1_back.domain.user.repository;

import com.example.swyp_team1_back.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); //이메일로 User 찾기

    Optional<User> findById(Long id);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);


}
