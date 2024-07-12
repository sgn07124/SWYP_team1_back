package com.example.swyp_team1_back.domain.tip.repository;

import com.example.swyp_team1_back.domain.tip.entity.Tip;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface TipRepository extends JpaRepository<Tip, Long> {
    Optional<Tip> findById(Long id);
    List<Tip> findAllByCompleteYNFalse();

    @Query("select t from Tip t where t.id > :cursor and t.completeYN = false and t.user.id = :userId order by t.id ASC")
    List<Tip> findByIdGreaterThanAndCompleteYNIsFalseAAndUserId(Long cursor, Long userId, Pageable pageable);

    @Query("SELECT t FROM Tip t WHERE t.id > :cursor AND t.completeYN = true AND t.user.id = :userId ORDER BY t.completeRegDate DESC")
    List<Tip> findByIdGreaterThanAndCompleteYNIsTrueAndUserIdOrderByCompleteRegDateDesc(Long cursor, Long userId, Pageable pageable);
}
