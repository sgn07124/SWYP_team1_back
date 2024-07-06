package com.example.swyp_team1_back.domain.bookmark.entity;

import com.example.swyp_team1_back.domain.tip.entity.Tip;
import com.example.swyp_team1_back.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "BookmarkTip")
@NoArgsConstructor
public class BookmarkTip extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bmtId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bmId")  // 주인
    private Bookmark bookmark;

    @ManyToOne
    @JoinColumn(name = "tipId")  // 주인
    private Tip tip;
}
