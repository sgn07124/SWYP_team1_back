package com.example.swyp_team1_back.domain.bookmark.service;

import com.example.swyp_team1_back.domain.bookmark.entity.Bookmark;
import com.example.swyp_team1_back.domain.bookmark.repository.BookmarkRepository;
import com.example.swyp_team1_back.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public void createBookmark(User user) {
        Bookmark bookmark = Bookmark.createBookmark(user);
        bookmarkRepository.save(bookmark);
    }
}
