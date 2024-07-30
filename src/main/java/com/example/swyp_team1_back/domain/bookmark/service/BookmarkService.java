package com.example.swyp_team1_back.domain.bookmark.service;

import com.example.swyp_team1_back.domain.bookmark.entity.Bookmark;
import com.example.swyp_team1_back.domain.bookmark.entity.BookmarkTip;
import com.example.swyp_team1_back.domain.bookmark.repository.BookmarkRepository;
import com.example.swyp_team1_back.domain.bookmark.repository.BookmarkTipRepository;
import com.example.swyp_team1_back.domain.tip.entity.Tip;
import com.example.swyp_team1_back.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkTipRepository bookmarkTipRepository;

    public void createBookmark(User user) {
        Bookmark bookmark = Bookmark.createBookmark(user);
        bookmarkRepository.save(bookmark);
    }

    /**
     * 북마크에 팁 추가
     * @param user
     * @param tip
     */
    @Transactional
    public void addTip(User user, Tip tip) {
        Bookmark bookmark = bookmarkRepository.findByUserId(user.getId());
        // 북마크가 비어있다면 새로 생성
        if (bookmark == null) {
            bookmark = Bookmark.createBookmark(user);
            bookmarkRepository.save(bookmark);
        }

        // BookmarkTip 생성
        BookmarkTip bookmarkTip = bookmarkTipRepository.findByBookmarkIdAndTipId(bookmark.getId(), tip.getId());
        // BookmarkTip이 비어있다면 새로 생성
        if (bookmarkTip == null) {
            bookmarkTip = BookmarkTip.createBookmarkTip(bookmark, tip);
            bookmarkTipRepository.save(bookmarkTip);
        }
    }
}
