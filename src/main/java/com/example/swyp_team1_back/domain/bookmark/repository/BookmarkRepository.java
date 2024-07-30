package com.example.swyp_team1_back.domain.bookmark.repository;

import com.example.swyp_team1_back.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
