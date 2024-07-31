package com.example.swyp_team1_back.domain.bookmark.repository;

import com.example.swyp_team1_back.domain.bookmark.entity.BookmarkTip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkTipRepository extends JpaRepository<BookmarkTip, Long> {

    BookmarkTip findByBookmarkIdAndTipId(Long bookmarkId, Long TipId);

    Optional<BookmarkTip> deleteByBookmarkIdAndTipId(Long bookmarkId, Long TipId);
}
