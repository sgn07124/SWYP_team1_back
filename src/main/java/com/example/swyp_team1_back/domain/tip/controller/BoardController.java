package com.example.swyp_team1_back.domain.tip.controller;

import com.example.swyp_team1_back.domain.tip.dto.request.CreateTipDTO;
import com.example.swyp_team1_back.domain.tip.dto.response.BoardTipDTO;
import com.example.swyp_team1_back.domain.tip.dto.response.TipCompleteYnListDTO;
import com.example.swyp_team1_back.domain.tip.service.BoardService;
import com.example.swyp_team1_back.domain.tip.service.TipUserService;
import com.example.swyp_team1_back.global.common.response.CursorPaginationResponse;
import com.example.swyp_team1_back.global.common.response.ErrorCode;
import com.example.swyp_team1_back.global.common.response.Response;
import com.example.swyp_team1_back.global.common.response.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/api/board")
@RequiredArgsConstructor
@Tag(name = "팁 게시판 관련 API", description = "다른 회원들이 등록한 팁들을 확인할 수 있음")
public class BoardController {

    private final BoardService boardService;
    private final TipUserService tipUserService;

    @GetMapping("/search")
    @Operation(summary = "검색으로 게시글 조회", description = "검색어를 입력하여 관련된 팁들을 무한스크롤로 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색어로 팁 조회 성공"),
            @ApiResponse(responseCode = "3002", description = "팁 조회 실패 - 팁을 찾을 수 없음")
    })
    public ResponseEntity<? extends Response<? extends Object>> getBoardSearchList(
            @Parameter(description = "다음 페이지 조회 시 필요한 커서 값으로 payload의 nextCursor값을 위치시킨다. 처음 조회 시(doing 탭의 가장 최근 팁들 4개 조회)에는 생략(\"/api/tip/doing\") ", required = false) @RequestParam(value = "cursor", required = false) Long cursor,
            @Parameter(description = "페이지 크기(기본값: 5)로 생략", required = false) @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
            @Parameter(description = "검색어 입력", required = true) @RequestParam(value = "keyword", required = true) String keyword)
    {
        try {
            List<BoardTipDTO> tips = boardService.getBoardSearch(cursor, pageSize, keyword);
            Long nextCursor = tips.isEmpty() ? null : tips.get(tips.size() - 1).getId();
            CursorPaginationResponse<BoardTipDTO> response = new CursorPaginationResponse<>(nextCursor, pageSize, tips);
            return ResponseUtil.createSuccessResponseWithPayload("검색어로 팁 조회 성공", response);
        } catch (Exception e) {
            return ResponseUtil.createExceptionResponse("팁 조회 실패", ErrorCode.FAIL_FIND_TIP, e.getMessage());
        }
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "카테고리로 팁 조회", description = "카테고리를 선택하여 해당되는 팁들을 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 조회 성공"),
            @ApiResponse(responseCode = "3002", description = "팁 조회 실패 - 팁을 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "categoryId", description = "카테고리 Id를 입력해주세요")
    })
    public ResponseEntity<? extends Response<? extends Object>> getBoardCategoryList(
            @Parameter(description = "다음 페이지 조회 시 필요한 커서 값으로 payload의 nextCursor값을 위치시킨다. 처음 조회 시(doing 탭의 가장 최근 팁들 4개 조회)에는 생략(\"/api/tip/doing\") ", required = false) @RequestParam(value = "cursor", required = false) Long cursor,
            @Parameter(description = "페이지 크기(기본값: 5)로 생략", required = false) @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
            @PathVariable Long categoryId)
    {
        try {
            List<BoardTipDTO> tips = boardService.getBoardCategory(cursor, pageSize, categoryId);
            Long nextCursor = tips.isEmpty() ? null : tips.get(tips.size() - 1).getId();
            CursorPaginationResponse<BoardTipDTO> response = new CursorPaginationResponse<>(nextCursor, pageSize, tips);
            return ResponseUtil.createSuccessResponseWithPayload("카테고리로 팁 조회 성공", response);
        } catch (Exception e) {
            return ResponseUtil.createExceptionResponse("팁 조회 실패", ErrorCode.FAIL_FIND_TIP, e.getMessage());
        }
    }

    @PostMapping("/submit")
    @Operation(summary = "다른 사람의 팁 등록", description = "로그인 한 회원은 게시판에서 다른 사람이 올린 팁을 등록할 수 있다. 등록한 팁은 게시판에 공개되지 않는다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팁 등록 성공"),
            @ApiResponse(responseCode = "3001", description = "팁 등록 실패")
    })
    @Parameters({
            @Parameter(name = "tipLink", description = "링크를 삽입해주세요."),
            @Parameter(name = "tipTitle", description = "팁의 제목을 적어주세요."),
            @Parameter(name = "category", description = "카테고리를 설정해주세요."),
            @Parameter(name = "actCnt", description = "실천 횟수를 적어주세요."),
            @Parameter(name = "deadLine_start", description = "데드라인을 설정해주세요."),
            @Parameter(name = "deadLine_end", description = "데드라인을 설정해주세요.")
    })
    public ResponseEntity<Response<Void>> createTip(@RequestBody CreateTipDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        try {
            tipUserService.createUserTip(dto, username, false);
            return ResponseUtil.createSuccessResponseWithoutPayload("팁 등록 성공");
        } catch (Exception e) {
            return ResponseUtil.createExceptionResponse("팁 등록 실패", ErrorCode.FAIL_CREATE_USER_TIP, e.getMessage());
        }
    }
}
