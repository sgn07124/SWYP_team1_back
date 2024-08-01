package com.example.swyp_team1_back.domain.bookmark.repository;

import com.example.swyp_team1_back.domain.bookmark.entity.BookmarkTip;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkTipRepository extends JpaRepository<BookmarkTip, Long> {

    BookmarkTip findByBookmarkIdAndTipId(Long bookmarkId, Long TipId);

    Optional<BookmarkTip> deleteByBookmarkIdAndTipId(Long bookmarkId, Long TipId);

    @Query("SELECT bt FROM BookmarkTip bt WHERE bt.bookmark.id = :bookmarkId AND (:cursor IS NULL OR bt.tip.id < :cursor) ORDER BY bt.regDate DESC")
    List<BookmarkTip> findByBookmarkIdAndCursor(@Param("bookmarkId") Long bookmarkId, @Param("cursor") Long cursor, Pageable pageable);
}
