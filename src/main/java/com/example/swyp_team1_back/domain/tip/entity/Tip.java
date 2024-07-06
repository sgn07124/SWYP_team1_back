package com.example.swyp_team1_back.domain.tip.entity;

import com.example.swyp_team1_back.domain.bookmark.entity.BookmarkTip;
import com.example.swyp_team1_back.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "Tip")
@NoArgsConstructor
public class Tip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipId")
    private Long id;

    @Column(name = "tipTitle", nullable = false)
    private String tipTitle;

    @Column(name = "tipLink", nullable = false)
    private String tipLink;

    @Column(name = "deadline_start", nullable = false)
    private LocalDate deadLine_start;  // 데드라인(시작일) : 날짜 형식으로 저장

    @Column(name = "deadline_end", nullable = false)
    private LocalDate deadLine_end;  // 데드라인(종료일) : 날짜 형식으로 저장

    @Column(name = "actCnt", nullable = false)
    private int actCnt;

    @Column(name = "complete_YN", nullable = false)
    private boolean completeYN;  // 완료 여부. true:완료. False:진행중

    @Column(name = "is_Mine", nullable = false)
    private boolean isMine;  // 내가 작성한 꿀팁인지 아닌지. true:내꺼. false:남에꺼

    @OneToMany(mappedBy = "tip")
    private List<BookmarkTip> bookmarkTips = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id")  // 주인
    private User user;

    @ManyToOne
    @JoinColumn(name = "cateId")  // 주인
    private Category category;


}
