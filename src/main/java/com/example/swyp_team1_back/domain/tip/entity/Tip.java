package com.example.swyp_team1_back.domain.tip.entity;

import com.example.swyp_team1_back.domain.bookmark.entity.BookmarkTip;
import com.example.swyp_team1_back.domain.tip.dto.request.CreateTipDTO;
import com.example.swyp_team1_back.domain.user.entity.User;
import com.example.swyp_team1_back.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Column(name = "tipLink", nullable = false, length = 2048)
    private String tipLink;

    @Column(name = "deadline_start", nullable = false)
    private LocalDate deadLine_start;  // 데드라인(시작일) : 날짜 형식으로 저장

    @Column(name = "deadline_end", nullable = false)
    private LocalDate deadLine_end;  // 데드라인(종료일) : 날짜 형식으로 저장

    @Column(name = "actCnt", nullable = false)
    private int actCnt;

    @Column(name = "actCnt_checked", nullable = false)
    private int actCntChecked;

    @Column(name = "complete_YN", nullable = false)
    private Boolean completeYN;  // 완료 여부. true:완료. False:진행중

    @Column(name = "is_Mine", nullable = false)
    private Boolean isMine;  // 내가 작성한 꿀팁인지 아닌지. true:내꺼. false:남에꺼

    @Column(name = "complete_reg_date")
    private LocalDateTime completeRegDate;  // completeYN이 true로 변환된 시간

    @OneToMany(mappedBy = "tip")
    private List<BookmarkTip> bookmarkTips = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id")  // 주인
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cateId")  // 주인
    private Category category;

    public static Tip createUserTip(CreateTipDTO dto) {
        Tip tip = createTip(dto);
        tip.isMine = true;
        return tip;
    }

    public static Tip createOtherTip(CreateTipDTO dto) {
        Tip tip = createTip(dto);
        tip.isMine = false;
        return tip;
    }

    private static Tip createTip(CreateTipDTO dto) {
        Tip tip = new Tip();
        tip.tipLink = dto.getTipLink();
        tip.tipTitle = dto.getTipTitle();
        tip.actCnt = dto.getActCnt();
        tip.actCntChecked = 0;
        tip.deadLine_start = LocalDate.parse(dto.getDeadLine_start());
        tip.deadLine_end = LocalDate.parse(dto.getDeadLine_end());

        tip.completeYN = false;
        return tip;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void updateTip(CreateTipDTO dto, Category category) {
        this.tipLink = dto.getTipLink();
        this.tipTitle = dto.getTipTitle();
        this.actCnt = dto.getActCnt();
        this.deadLine_start = LocalDate.parse(dto.getDeadLine_start());
        this.deadLine_end = LocalDate.parse(dto.getDeadLine_end());
        this.setCategory(category);

        // actCntChecked가 actCnt보다 크면 actCnt와 같게 설정
        if (this.actCntChecked > this.actCnt) {
            this.actCntChecked = this.actCnt;
        }

        updateCompleteStatus();
    }

    /**
     * 팁 수정 시, 완료 상태 반영
     */
    private void updateCompleteStatus() {
        if (this.actCntChecked >= this.actCnt) {
            this.completeYN = true;
            this.completeRegDate = LocalDateTime.now();
        } else if (LocalDate.now().isBefore(this.deadLine_end)) {
            this.completeYN = false;
        }
    }

    public void updateActCntChecked(int actCntChecked) {
        this.actCntChecked = actCntChecked;
        if (this.actCntChecked == this.actCnt) {  // 데드라인이 남아 있지만, 현재 진행 중인 실천 횟수와 총 실천 횟수가 같아진 경우.
            this.completeYN = true;
            this.completeRegDate = LocalDateTime.now();
        } else if (this.actCntChecked < this.actCnt && LocalDate.now().isBefore(this.deadLine_end)) {  // 데드라인이 남아 있고, 체크가 다시 해제된 경우
            this.completeYN = false;
        }
    }

    /**
     * 현재 날짜가 deadLine_end를 넘어간 경우
     */
    public void checkCompleteStatus() {
        LocalDate now = LocalDate.now();
        if (now.isAfter(this.deadLine_end)) {
            this.completeYN = true;
            this.completeRegDate = LocalDateTime.now();
        } else {
            this.completeYN = false;
        }
    }

    public void setUser(User user) {
        this.user = user;
    }
}
