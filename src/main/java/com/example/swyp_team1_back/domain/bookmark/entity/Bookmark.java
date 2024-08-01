package com.example.swyp_team1_back.domain.bookmark.entity;

import com.example.swyp_team1_back.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "Bookmark")
@NoArgsConstructor
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bmId")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id")
    private User user;

    @OneToMany(mappedBy = "bookmark")
    private List<BookmarkTip> bookmarkTips = new ArrayList<>();

    public static Bookmark createBookmark(User user) {
        Bookmark bookmark = new Bookmark();
        bookmark.user = user;
        return bookmark;
    }
}
