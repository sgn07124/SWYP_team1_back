package com.example.swyp_team1_back.domain.tip.controller;

import com.example.swyp_team1_back.domain.tip.dto.request.CreateTipDTO;
import com.example.swyp_team1_back.domain.tip.dto.request.UpdateTipCntCheckedDTO;
import com.example.swyp_team1_back.domain.tip.dto.response.TipCompleteYnListDTO;
import com.example.swyp_team1_back.domain.tip.dto.response.TipDetailDTO;
import com.example.swyp_team1_back.domain.tip.entity.Tip;
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
@RequestMapping("/api/tip")
@RequiredArgsConstructor
@Tag(name = "로그인한 사용자의 팁 관련 API", description = "팁 등록, 수정, 삭제, 상세 조회, 목록 조회(진행 중, 완료)")
public class TipUserController {

    private final TipUserService tipUserService;

    @PostMapping("/submit")
    @Operation(summary = "직접 팁 등록", description = "로그인 한 회원은 직접 팁을 등록할 수 있다. 등록한 팁은 게시판에 공개된다.")
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
            tipUserService.createUserTip(dto, username);
            return ResponseUtil.createSuccessResponseWithoutPayload("팁 등록 성공");
        } catch (Exception e) {
            return ResponseUtil.createExceptionResponse("팁 등록 실패", ErrorCode.FAIL_CREATE_USER_TIP, e.getMessage());
        }
    }

    @DeleteMapping("/{tip_id}")
    @Operation(summary = "팁 삭제", description = "팁 id 값으로 팁을 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팁 삭제 성공"),
            @ApiResponse(responseCode = "3002", description = "팁 삭제 실패 - 팁을 찾을 수 없음")
    })
    public ResponseEntity<Response<Void>> deleteTip(@PathVariable Long tip_id) {
        try {
            tipUserService.deleteTip(tip_id);
            return ResponseUtil.createSuccessResponseWithoutPayload("팁 삭제 성공");
        } catch (Exception e) {
            return ResponseUtil.createExceptionResponse("팁 삭제 실패", ErrorCode.FAIL_FIND_TIP, e.getMessage());
        }
    }

    @GetMapping("/{tip_id}")
    @Operation(summary = "팁 상세 조회", description = "팁 id 값으로 팁을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팁 조회 성공"),
            @ApiResponse(responseCode = "3002", description = "팁 조회 실패 - 팁을 찾을 수 없음")
    })
    public ResponseEntity<? extends Response<? extends Object>> getTipDetail(@PathVariable Long tip_id) {
        try {
            TipDetailDTO tipDetailDTO = tipUserService.getTipDetail(tip_id);
            return ResponseUtil.createSuccessResponseWithPayload("팁 조회 성공", tipDetailDTO);
        } catch (Exception e) {
            return ResponseUtil.createExceptionResponse("팁 조회 실패", ErrorCode.FAIL_FIND_TIP, e.getMessage());
        }
    }

    @PutMapping("/{tip_id}")
    @Operation(summary = "팁 수정", description = "로그인 한 회원은 tip_id에 해당되는 팁을 수정할 수 있다. 팁 수정 시, 팁 상세 조회로 팁을 가져온 뒤 수정이 되어야 한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팁 수정 성공"),
            @ApiResponse(responseCode = "3003", description = "팁 수정 실패 - 팁을 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "tipLink", description = "링크를 삽입해주세요."),
            @Parameter(name = "tipTitle", description = "팁의 제목을 적어주세요."),
            @Parameter(name = "category", description = "카테고리를 설정해주세요."),
            @Parameter(name = "actCnt", description = "실천 횟수를 적어주세요."),
            @Parameter(name = "deadLine_start", description = "데드라인을 설정해주세요."),
            @Parameter(name = "deadLine_end", description = "데드라인을 설정해주세요.")
    })
    public ResponseEntity<? extends Response<? extends Object>> updateTip(@PathVariable Long tip_id, @RequestBody CreateTipDTO dto) {
        try {
            tipUserService.updateTip(tip_id, dto);
            return ResponseUtil.createSuccessResponseWithoutPayload("팁 수정 성공");
        } catch (Exception e) {
            return ResponseUtil.createExceptionResponse("팁 수정 실패", ErrorCode.FAIL_UPDATE_TIP, e.getMessage());
        }
    }

    @PatchMapping("/{tip_id}/actCnt")
    @Operation(summary = "실천한 체크 횟수 update", description = "실천하여 체크된 체크박스의 개수로 요청해야 한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "실천 횟수 update 성공"),
            @ApiResponse(responseCode = "3004", description = "실천 횟수 update 실패")
    })
    @Parameters({
            @Parameter(name = "tipCntChecked", description = "실천한 체크 횟수를 입력해주세요.")
    })
    public ResponseEntity<? extends Response<? extends Object>> updateActCntChecked(@PathVariable Long tip_id, @RequestBody UpdateTipCntCheckedDTO dto) {
        try {
            tipUserService.updateActCntChecked(tip_id, dto.getTipCntChecked());
            return ResponseUtil.createSuccessResponseWithoutPayload("팁 수정 성공");
        } catch (Exception e) {
            return ResponseUtil.createExceptionResponse("팁 수정 실패", ErrorCode.FAIL_UPDATE_TIP_ACT_CNT, e.getMessage());
        }
    }

    @GetMapping("/doing")
    @Operation(summary = "마이페이지의 팁 실천 중 리스트 조회", description = "실천 중인 팁을 최근 등록한 실천 리스트 순으로 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "실천 중인 팁 조회 성공"),
            @ApiResponse(responseCode = "3002", description = "팁 조회 실패 - 팁을 찾을 수 없음")
    })
    public ResponseEntity<? extends Response<? extends Object>> getDoingTipList(
            @Parameter(description = "다음 페이지 조회 시 필요한 커서 값으로 payload의 nextCursor값을 위치시킨다. 처음 조회 시(doing 탭의 가장 최근 팁들 4개 조회)에는 생략(\"/api/tip/doing\") ", required = false) @RequestParam(value = "cursor", required = false) Long cursor,
            @Parameter(description = "페이지 크기(기본값: 4)로 생략", required = false) @RequestParam(value = "pageSize", defaultValue = "4") int pageSize) {
        try {
            List<TipCompleteYnListDTO> tips = tipUserService.getDoingTips(cursor, pageSize);
            Long nextCursor = tips.isEmpty() ? null : tips.get(tips.size() - 1).getId();
            CursorPaginationResponse<TipCompleteYnListDTO> response = new CursorPaginationResponse<>(nextCursor, pageSize, tips);
            return ResponseUtil.createSuccessResponseWithPayload("팁 조회 성공", response);
        } catch (Exception e) {
            return ResponseUtil.createExceptionResponse("팁 조회 실패", ErrorCode.FAIL_FIND_TIP, e.getMessage());
        }
    }

    @GetMapping("/finish")
    @Operation(summary = "마이페이지의 팁 실천 완료 리스트 조회", description = "실천 완료된 팁을 최근 완료된 팁 리스트 순으로 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "완료된 팁 조회 성공"),
            @ApiResponse(responseCode = "3002", description = "팁 조회 실패 - 팁을 찾을 수 없음")
    })
    public ResponseEntity<? extends Response<? extends Object>> getFinishTipList(
            @Parameter(description = "다음 페이지 조회 시 필요한 커서 값으로 payload의 nextCursor값을 위치시킨다. 처음 조회 시(doing 탭의 가장 최근 팁들 4개 조회)에는 생략(\"/api/tip/doing\") ", required = false) @RequestParam(value = "cursor", required = false) Long cursor,
            @Parameter(description = "페이지 크기(기본값: 4)로 생략", required = false) @RequestParam(value = "pageSize", defaultValue = "4") int pageSize) {
        try {
            List<TipCompleteYnListDTO> tips = tipUserService.getFinishTips(cursor, pageSize);
            Long nextCursor = tips.isEmpty() ? null : tips.get(tips.size() - 1).getId();
            CursorPaginationResponse<TipCompleteYnListDTO> response = new CursorPaginationResponse<>(nextCursor, pageSize, tips);
            return ResponseUtil.createSuccessResponseWithPayload("팁 조회 성공", response);
        } catch (Exception e) {
            return ResponseUtil.createExceptionResponse("팁 조회 실패", ErrorCode.FAIL_FIND_TIP, e.getMessage());
        }
    }
}
