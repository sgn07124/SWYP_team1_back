package com.example.swyp_team1_back.domain.tip.controller;

import com.example.swyp_team1_back.domain.tip.dto.CreateTipDTO;
import com.example.swyp_team1_back.domain.tip.service.TipUserService;
import com.example.swyp_team1_back.domain.user.dto.CreateUserDTO;
import com.example.swyp_team1_back.global.common.response.ErrorCode;
import com.example.swyp_team1_back.global.common.response.Response;
import com.example.swyp_team1_back.global.common.response.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
@RequestMapping("/api/tip")
@RequiredArgsConstructor
@Tag(name = "로그인한 사용자의 팁 관련 API", description = "팁 등록, 수정, 삭제, 상세 조회, 목록 조회(진행 중, 완료)")
public class TipUserController {

    private final TipUserService tipUserService;

    @PostMapping()
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
    public ResponseEntity<Response<Void>> createTip(@RequestBody CreateTipDTO dto) {  // SecurityConfig의 경로 인가 수정 필요
        try {
            tipUserService.createUserTip(dto);
            return ResponseUtil.createSuccessResponseWithoutPayload("팁 등록 성공");
        } catch (Exception e) {
            return ResponseUtil.createExceptionResponse("팁 등록 실패", ErrorCode.FAIL_CREATE_USER_TIP, e.getMessage());
        }
    }

    @DeleteMapping("/{tip_id}")
    @Operation(summary = "팁 삭제", description = "팁 id 값으로 팁을 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팁 삭제 성공"),
            @ApiResponse(responseCode = "3002", description = "팁 삭제 실패")
    })
    public ResponseEntity<Response<Void>> deleteTip(@PathVariable Long tip_id) {  // SecurityConfig의 경로 인가 수정 필요
        try {
            tipUserService.deleteTip(tip_id);
            return ResponseUtil.createSuccessResponseWithoutPayload("팁 삭제 성공");
        } catch (Exception e) {
            return ResponseUtil.createExceptionResponse("팁 삭제 실패", ErrorCode.FAIL_FIND_TIP, e.getMessage());
        }
    }
}
