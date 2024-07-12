package com.example.swyp_team1_back.global.common.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CursorPaginationResponse<T> {

    private Long nextCursor;  // 다음 커서 위치(다음 페이지를 조회하기 위한 용도)
    private int pageSize;
    private List<T> contents;

    public CursorPaginationResponse(Long nextCursor, int pageSize, List<T> contents) {
        this.nextCursor = nextCursor;
        this.pageSize = pageSize;
        this.contents = contents;
    }
}
