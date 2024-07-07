package com.example.swyp_team1_back.domain.user.repository;

import com.example.swyp_team1_back.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
