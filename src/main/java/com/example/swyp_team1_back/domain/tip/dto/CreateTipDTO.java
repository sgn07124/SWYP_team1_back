package com.example.swyp_team1_back.domain.tip.dto;

import com.example.swyp_team1_back.domain.tip.entity.Category;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateTipDTO {

    private String tipLink;

    private String tipTitle;

    private Long categoryId;

    private int actCnt;

    private String deadLine_start;
    private String deadLine_end;


}
