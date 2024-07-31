package com.example.swyp_team1_back.domain.bookmark.dto.response;

import com.example.swyp_team1_back.domain.bookmark.entity.BookmarkTip;
import com.example.swyp_team1_back.domain.tip.entity.Tip;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipListDto {

    private Long id;
    private String title;
    private String category;

    public static TipListDto toEntity(BookmarkTip bookmarkTip) {
        Tip tip = bookmarkTip.getTip();
        TipListDto dto = new TipListDto();
        dto.setId(tip.getId());
        dto.setTitle(tip.getTipTitle());
        dto.setCategory(tip.getCategory().getCategoryName());
        return dto;
    }
}
