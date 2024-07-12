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

    @Query("select t from Tip t where t.id > :cursor and t.completeYN = false order by t.id ASC")
    List<Tip> findByIdGreaterThanAndCompleteYNIsFalse(Long cursor, Pageable pageable);

    @Query("select t from Tip t where t.id > :cursor and t.completeYN = true order by t.id ASC")
    List<Tip> findByIdGreaterThanAndCompleteYNIsTrue(Long cursor, Pageable pageable);
}
