package com.example.swyp_team1_back;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "서버 테스트용", description = "서버 테스트 및 스웨거 적용 테스트")
public class TestController {

    @GetMapping("/")
    @Operation(summary = "메인 페이지", description = "메인 페이지ㅣㅣㅣㅣㅣ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "요청 실패.", content = @Content(mediaType = "application/json")),
    })
    public String home() {
        return "Hello swyp team1!!!";
    }
}
