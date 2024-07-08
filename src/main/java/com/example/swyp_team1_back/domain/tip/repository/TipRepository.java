package com.example.swyp_team1_back.domain.tip.repository;

import com.example.swyp_team1_back.domain.tip.entity.Tip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipRepository extends JpaRepository<Tip, Long> {
}
