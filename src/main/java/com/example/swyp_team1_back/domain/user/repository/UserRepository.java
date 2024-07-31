package com.example.swyp_team1_back.domain.user.repository;

import com.example.swyp_team1_back.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); //이메일로 User 찾기

    Optional<User> findById(Long id);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    boolean existsByNickname(String nickname);

    boolean existsByEmailAndNameAndPhone(String email, String name, String phone);

    @Modifying
    @Query("UPDATE User u SET u.isDeleted = :isDeleted WHERE u.email = :email")
    void updateStatusByEmail(@Param("email") String email, @Param("isDeleted") boolean isDeleted);

}
