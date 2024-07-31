package com.example.swyp_team1_back.domain.bookmark.service;

import com.example.swyp_team1_back.domain.bookmark.entity.Bookmark;
import com.example.swyp_team1_back.domain.bookmark.entity.BookmarkTip;
import com.example.swyp_team1_back.domain.bookmark.repository.BookmarkRepository;
import com.example.swyp_team1_back.domain.bookmark.repository.BookmarkTipRepository;
import com.example.swyp_team1_back.domain.tip.entity.Tip;
import com.example.swyp_team1_back.domain.user.entity.User;
import com.example.swyp_team1_back.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkTipRepository bookmarkTipRepository;
    private final UserRepository userRepository;

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

    /**
     * 북마크에서 팁 삭제
     * @param tipId
     */
    @Transactional
    public void deleteTip(Long tipId) {
        Long currentUser = getCurrentUserId();

        Bookmark bookmark = bookmarkRepository.findByUserId(currentUser);
        if (bookmark == null) throw new IllegalStateException("현재 사용자에 대한 북마크를 찾을 수 없습니다.");

        Optional<BookmarkTip> bookmarkTipOptional = Optional.ofNullable(bookmarkTipRepository.findByBookmarkIdAndTipId(bookmark.getId(), tipId));
        if (bookmarkTipOptional.isEmpty()) throw new IllegalArgumentException("북마크에 팁이 없습니다.");

        bookmarkTipRepository.deleteByBookmarkIdAndTipId(bookmark.getId(), tipId);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // UserDetails의 getUsername()이 호출되어 email이 반환됩니다.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + email));
        return user.getId();
    }
}
