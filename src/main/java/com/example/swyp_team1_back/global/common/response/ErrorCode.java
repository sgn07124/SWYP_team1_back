package com.example.swyp_team1_back.global.common.response;

public enum ErrorCode {

    SUCCESS(200, "SUCCESS"),

    // 클라이언트 에러 코드
    VALIDATION_ERROR(4001, "Validation Error"),
    ILLEGAL_ARGUMENT_ERROR(4002, "Illegal Argument Error"),
    ILLEGAL_STATE_ERROR(4003, "Illegal State Error"),

    // 서버 에러 코드
    INTERNAL_SERVER_ERROR(5001, "Internal Server Error");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
