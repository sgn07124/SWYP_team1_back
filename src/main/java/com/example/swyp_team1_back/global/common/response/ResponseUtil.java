package com.example.swyp_team1_back.global.common.response;

import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseUtil {

    public static <T> ResponseEntity<Response<T>> createSuccessResponseWithPayload(String message, T payload) {
        return ResponseEntity.ok(Response.successWithPayload(message, payload));
    }

    public static ResponseEntity<Response<Void>> createSuccessResponseWithoutPayload(String message) {
        return ResponseEntity.ok(Response.successWithoutPayload(message));
    }

    public static ResponseEntity<Response<Void>> createValidationErrorResponse(String message, List<Response.ErrorDetail> errors) {
        return ResponseEntity.badRequest().body(Response.validationError(message, errors));
    }

    public static ResponseEntity<Response<Void>> createExceptionResponse(String message, ErrorCode errorCode, String field, String reason) {
        return ResponseEntity.badRequest().body(Response.exceptionError(message, errorCode, field, reason));
    }

    public static ResponseEntity<Response<Void>> createExceptionResponse(String message, ErrorCode errorCode, String reason) {
        return createExceptionResponse(message, errorCode, null, reason);
    }


}
