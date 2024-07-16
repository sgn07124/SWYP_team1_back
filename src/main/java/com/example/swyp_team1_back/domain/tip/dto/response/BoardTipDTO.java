package com.example.swyp_team1_back.domain.tip.dto.response;

import com.example.swyp_team1_back.domain.tip.entity.Tip;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardTipDTO {

    private Long id;
    private String title;
    private String categoryName;

    public BoardTipDTO(Tip tip) {
        this.id = tip.getId();
        this.title = tip.getTipTitle();
        this.categoryName = tip.getCategory().getCategoryName();
    }
}
