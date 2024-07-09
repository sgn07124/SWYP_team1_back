package com.example.swyp_team1_back.global.common.response;

public enum ErrorCode {

    SUCCESS(200, "Success"),

    // 유효성 에러
    VALIDATION_ERROR(4001, "Validation Error"),

    // USER 관련 예외처리 에러
    EMAIL_ALREADY_EXISTS(4002, "Email Already Exists"),
    PASSWORD_UNMATCHED(4003, "Password Unmatched"),
    PHONE_ALREADY_EXISTS(4004, "Phone Already Exists"),
    AGREE_TOS_NOT_CHECKED(4005, "Agree TOS Not Checked"),
    AGREE_PICU_NOT_CHECKED(4006, "Agree PICU Not Checked"),
    ILLEGAL_ARGUMENT_ERROR(4007, "Illegal Argument Error"),
    ILLEGAL_STATE_ERROR(4008, "Illegal State Error"),

    FAIL_CREATE_USER_TIP(3001, "Fail to create user tip"),
    FAIL_FIND_TIP(3002, "Fail to find tip"),
    FAIL_UPDATE_TIP(3003, "Fail to update tip");

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
