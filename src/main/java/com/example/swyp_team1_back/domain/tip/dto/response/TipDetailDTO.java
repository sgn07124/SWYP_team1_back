package com.example.swyp_team1_back.domain.tip.dto.response;

import com.example.swyp_team1_back.domain.tip.entity.Tip;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class TipDetailDTO {

    private Long id;
    private String tipLink;
    private String tipTitle;
    private Long categoryId;
    private int actCnt;  // 실천 횟수
    private String deadLine_start;
    private String deadLine_end;

    private String categoryName;  // 카테고리 텍스트로 노출
    private String periodDate;  // 디데이 - 실천 기간 날짜 노출
    private int d_day;  // 디데이 - 현재 날짜부터 디데이까지 남은 날자를 노출
    private int actCnt_checked;  // 체크된 실천 횟수 개수

}
