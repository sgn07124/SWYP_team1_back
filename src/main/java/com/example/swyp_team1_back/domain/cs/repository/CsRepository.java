package com.example.swyp_team1_back.domain.cs.repository;

import com.example.swyp_team1_back.domain.cs.entity.Cs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CsRepository extends JpaRepository<Cs, Long> {
}
