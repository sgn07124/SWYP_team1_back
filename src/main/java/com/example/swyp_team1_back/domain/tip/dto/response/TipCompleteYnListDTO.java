package com.example.swyp_team1_back.domain.tip.dto.response;

import com.example.swyp_team1_back.domain.tip.entity.Tip;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
public class TipCompleteYnListDTO {

    private Long id;
    private String tipTitle;
    private int d_day;  // 디데이 - 현재 날짜부터 디데이까지 남은 날자를 노출
    private int presentPercent;  // 실천율 - actCnt_checked/actCnt로 백분율로 표현되어야 하며 백분율은 소수점 첫째자리에서 반올림

    public TipCompleteYnListDTO(Tip tip) {
        this.id = tip.getId();
        this.tipTitle = tip.getTipTitle();
        this.presentPercent = calculatePresentPercent(tip.getActCntChecked(), tip.getActCnt());
        this.d_day = (int) ChronoUnit.DAYS.between(LocalDate.now(), tip.getDeadLine_end());  // d-day
    }

    private int calculatePresentPercent(int actCntChecked, int actCnt) {
        if (actCnt == 0) {
            return 0;
        }
        double percent = ((double) actCntChecked / actCnt) * 100;
        return (int) Math.round(percent);
    }
}
