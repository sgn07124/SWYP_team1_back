package com.example.swyp_team1_back.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private boolean success;
    private String message;
    private int code;
    private T payload;
    private List<ErrorDetail> errors;

    @Getter @Setter
    @RequiredArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorDetail {
        private String field;
        private String reason;

        public ErrorDetail(String field, String reason) {
            this.field = field;
            this.reason = reason;
        }
    }

    public static <T> Response<T> successWithPayload(String message, T payload) {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setCode(ErrorCode.SUCCESS.getCode());
        response.setPayload(payload);
        response.setErrors(null);  // 성공 응답에는 errors를 null로 설정
        return response;
    }

    public static Response<Void> successWithoutPayload(String message) {
        Response<Void> response = new Response<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setCode(ErrorCode.SUCCESS.getCode());
        response.setPayload(null);  // payload를 null로 설정
        response.setErrors(null);  // 성공 응답에는 errors를 null로 설정
        return response;
    }

    public static Response<Void> validationError(String message, List<ErrorDetail> errors) {
        Response<Void> response = new Response<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setCode(ErrorCode.VALIDATION_ERROR.getCode());
        response.setPayload(null);  // 실패 응답에는 payload를 null로 설정
        response.setErrors(errors);
        return response;
    }

    public static Response<Void> exceptionError(String message, ErrorCode errorCode, String field, String reason) {
        Response<Void> response = new Response<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setCode(errorCode.getCode());
        response.setPayload(null);  // 실패 응답에는 payload를 null로 설정

        ErrorDetail errorDetail = new ErrorDetail(field, reason);
        response.setErrors(Collections.singletonList(errorDetail));
        return response;
    }
}

