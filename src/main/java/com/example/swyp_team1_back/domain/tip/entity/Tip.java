package com.example.swyp_team1_back.domain.tip.entity;

import com.example.swyp_team1_back.domain.bookmark.entity.BookmarkTip;
import com.example.swyp_team1_back.domain.tip.dto.CreateTipDTO;
import com.example.swyp_team1_back.domain.user.entity.User;
import com.example.swyp_team1_back.global.common.entity.BaseTimeEntity;
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
public class Tip extends BaseTimeEntity {

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
    private Boolean completeYN;  // 완료 여부. true:완료. False:진행중

    @Column(name = "is_Mine", nullable = false)
    private Boolean isMine;  // 내가 작성한 꿀팁인지 아닌지. true:내꺼. false:남에꺼

    @OneToMany(mappedBy = "tip")
    private List<BookmarkTip> bookmarkTips = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id")  // 주인
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cateId")  // 주인
    private Category category;

    public static Tip createUserTip(CreateTipDTO dto) {  // 로그인 구현되면 후처리 해야됨
        Tip tip = new Tip();
        tip.tipLink = dto.getTipLink();
        tip.tipTitle = dto.getTipTitle();
        tip.actCnt = dto.getActCnt();
        tip.deadLine_start = LocalDate.parse(dto.getDeadLine_start());
        tip.deadLine_end = LocalDate.parse(dto.getDeadLine_end());

        tip.completeYN = false;
        tip.isMine = true;
        return tip;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public static void deleteTip(Long tipId) {

    }


}
