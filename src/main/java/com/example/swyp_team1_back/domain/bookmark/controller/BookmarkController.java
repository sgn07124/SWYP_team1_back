package com.example.swyp_team1_back.domain.bookmark.controller;

import com.example.swyp_team1_back.domain.bookmark.dto.response.TipListDto;
import com.example.swyp_team1_back.domain.bookmark.service.BookmarkService;
import com.example.swyp_team1_back.domain.tip.dto.response.TipCompleteYnListDTO;
import com.example.swyp_team1_back.domain.tip.entity.Tip;
import com.example.swyp_team1_back.domain.tip.repository.TipRepository;
import com.example.swyp_team1_back.domain.tip.service.TipUserService;
import com.example.swyp_team1_back.domain.user.entity.User;
import com.example.swyp_team1_back.domain.user.repository.UserRepository;
import com.example.swyp_team1_back.global.common.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/api/bookmark")
@RequiredArgsConstructor
@Tag(name = "로그인한 사용자의 북마크 관련 API", description = "북마크 등록, 삭제, 리스트로 조회")
public class BookmarkController {

    private final TipUserService tipUserService;
    private final UserRepository userRepository;
    private final TipRepository tipRepository;
    private final BookmarkService bookmarkService;

    @PostMapping("/{tip_id}")
    @Operation(summary = "북마크에 팁 추가", description = "사용자는 저장하고 싶은 팁을 북마크에 저장할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "북마크에 팁 추가 성공"),
            @ApiResponse(responseCode = "3002", description = "북마크에 팁 추가 실패 - 팁을 찾을 수 없음"),
            @ApiResponse(responseCode = "4001", description = "북마크에 팁 추가 실패 - 사용자를 찾을 수 없음")
    })
    public ResponseEntity<Response<Void>> addTip(@PathVariable Long tip_id) {

        Long currentUser = getCurrentUserId();
        User user = userRepository.findById(currentUser).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Tip tip = tipRepository.findTipById(tip_id).orElseThrow(() -> new ResourceNotFoundException("Tip not found"));

        try {
            // 북마크에 팁 추가
            bookmarkService.addTip(user, tip);
            return ResponseUtil.createSuccessResponseWithoutPayload("북마크에 팁 추가 성공");
        } catch (Exception e) {
            if (user == null) return ResponseUtil.createExceptionResponse("사용자 조회 실패", ErrorCode.FAIL_FIND_USER, e.getMessage());
            if (tip == null) return ResponseUtil.createExceptionResponse("팁 조회 실패", ErrorCode.FAIL_FIND_TIP, e.getMessage());
            return ResponseUtil.createExceptionResponse("북마크에 팁 추가 실패", ErrorCode.FAIL_FIND_TIP, e.getMessage());
        }
    }

    @DeleteMapping("/{tip_id}")
    @Operation(summary = "북마크에서 팁 삭제", description = "사용자는 북마크에서 필요없는 팁을 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "북마크의 팁 삭제 성공"),
            @ApiResponse(responseCode = "3002", description = "북마크의 팁 삭제 실패 - 팁을 찾을 수 없음"),
            @ApiResponse(responseCode = "4001", description = "북마크에 팁 삭제 실패 - 사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "4003", description = "북마크에 팁 삭제 실패 - 북마크에서 팁을 찾을 수 없음"),
            @ApiResponse(responseCode = "4004", description = "북마크에 팁 삭제 실패 - 사용자의 북마크를 찾을 수 없음")
    })
    public ResponseEntity<Response<Void>> deleteTip(@PathVariable Long tip_id) {
        try {
            bookmarkService.deleteTip(tip_id);
            return ResponseUtil.createSuccessResponseWithoutPayload("북마크의 팁 삭제 성공");
        } catch (IllegalStateException e) {
            return ResponseUtil.createExceptionResponse("북마크의 팁 삭제 실패", ErrorCode.FAIL_FIND_BOOKMARK, e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseUtil.createExceptionResponse("북마크에서 팁 삭제 실패", ErrorCode.FAIL_FIND_BOOKMARK_TIP, e.getMessage());
        }
    }

    @GetMapping("/tips")
    @Operation(summary = "북마크의 팁 리스트로 조회", description = "등록한 북마크의 팁들을 리스트로 조회한다. 무한 스크롤이 적용된다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팁들 조회 성공"),
            @ApiResponse(responseCode = "3002", description = "팁들 조회 실패 - 팁을 찾을 수 없음")
    })
    public ResponseEntity<? extends Response<? extends Object>> getBookmarkList(
            @Parameter(description = "다음 페이지 조회 시 필요한 커서 값으로 payload의 nextCursor값을 위치시킨다. 처음 조회 시에는 생략", required = false) @RequestParam(value = "cursor", required = false) Long cursor,
            @Parameter(description = "페이지 크기(기본값: 4)로 생략", required = false) @RequestParam(value = "pageSize", defaultValue = "4") int pageSize) {
        try {
            System.out.println("Received cursor: " + cursor);
            List<TipListDto> tips = bookmarkService.getBookmarkTips(cursor, pageSize);
            Long nextCursor = tips.isEmpty() ? null : tips.get(tips.size() - 1).getId();

            CursorPaginationResponse<TipListDto> response = new CursorPaginationResponse<>(nextCursor, pageSize, tips);
            return ResponseUtil.createSuccessResponseWithPayload("팁 조회 성공", response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createExceptionResponse("팁 조회 실패", ErrorCode.FAIL_FIND_TIP, e.getMessage());
        }
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // UserDetails의 getUsername()이 호출되어 email이 반환됩니다.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + email));
        return user.getId();
    }
}
