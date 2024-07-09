package com.example.swyp_team1_back.global.common.response;

public class ResourceNotFoundException extends RuntimeException {
    private final String reason;

    public ResourceNotFoundException(String reason) {
        super(reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
