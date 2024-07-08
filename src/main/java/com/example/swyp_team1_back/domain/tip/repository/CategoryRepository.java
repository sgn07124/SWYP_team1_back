package com.example.swyp_team1_back.domain.tip.repository;

import com.example.swyp_team1_back.domain.tip.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
