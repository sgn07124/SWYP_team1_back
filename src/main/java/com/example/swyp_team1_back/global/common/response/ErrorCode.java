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
    EMAIL_NOT_FOUND(4009, "Email Not Found"),
    INVALID_PASSWORD(4010, "Invalid Password"),
    UNAUTHORIZED(4011, "Unauthorized"),
    NICKNAME_ALREADY_EXISTS(4012, "Nickname Already Exists"),
    NICKNAME_CHANGE_FAILED(4013, "Nickname Change Failed"),
    FAIL_CREATE_USER_TIP(3001, "Fail to create user tip"),
    FAIL_FIND_TIP(3002, "Fail to find tip"),
    FAIL_UPDATE_TIP(3003, "Fail to update tip"),
    FAIL_UPDATE_TIP_ACT_CNT(3004, "Fail to update tip act count"),
    FAIL_FIND_USER(4001, "Fail to find user"),
    FAIL_ADD_TIP_BOOKMARK(4002, "Fail To Add Tip To Bookmark"),
    FAIL_FIND_BOOKMARK_TIP(4003, "Fail to find tip at bookmark"),
    FAIL_FIND_BOOKMARK(4004, "Fail to find bookmark");

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
