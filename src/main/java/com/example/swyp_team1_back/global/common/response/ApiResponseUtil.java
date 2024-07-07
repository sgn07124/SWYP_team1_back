package com.example.swyp_team1_back.global.common.response;

import org.springframework.http.ResponseEntity;

import java.util.List;

public class ApiResponseUtil {

    public static <T> ResponseEntity<ApiResponse<T>> createSuccessResponseWithPayload(String message, T payload) {
        return ResponseEntity.ok(ApiResponse.successWithPayload(message, payload));
    }

    public static ResponseEntity<ApiResponse<Void>> createSuccessResponseWithoutPayload(String message) {
        return ResponseEntity.ok(ApiResponse.successWithoutPayload(message));
    }

    public static ResponseEntity<ApiResponse<Void>> createValidationErrorResponse(String message, List<ApiResponse.ErrorDetail> errors) {
        return ResponseEntity.badRequest().body(ApiResponse.validationError(message, errors));
    }

    public static ResponseEntity<ApiResponse<Void>> createExceptionResponse(String message, ErrorCode errorCode, String field, String reason) {
        return ResponseEntity.badRequest().body(ApiResponse.exceptionError(message, errorCode, field, reason));
    }

    public static ResponseEntity<ApiResponse<Void>> createExceptionResponse(String message, ErrorCode errorCode, String reason) {
        return createExceptionResponse(message, errorCode, null, reason);
    }
}
