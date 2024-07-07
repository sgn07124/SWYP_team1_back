package com.example.swyp_team1_back.global.common.response;

public class CustomFieldException extends RuntimeException {
    private final String field;
    private final ErrorCode errorCode;

    public CustomFieldException(String field, String message, ErrorCode errorCode) {
        super(message);
        this.field = field;
        this.errorCode = errorCode;
    }

    public String getField() {
        return field;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
