package com.example.swyp_team1_back.global.common.response;

public class CustomFieldException extends RuntimeException {
    private final String field;

    public CustomFieldException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
