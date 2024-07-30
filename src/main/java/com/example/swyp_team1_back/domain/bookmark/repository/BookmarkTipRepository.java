package com.example.swyp_team1_back.domain.bookmark.repository;

import com.example.swyp_team1_back.domain.bookmark.entity.BookmarkTip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkTipRepository extends JpaRepository<BookmarkTip, Long> {

    BookmarkTip findByBookmarkIdAndTipId(Long bookmarkId, Long TipId);
}
