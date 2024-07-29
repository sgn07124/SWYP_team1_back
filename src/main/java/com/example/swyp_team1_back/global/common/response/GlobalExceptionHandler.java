package com.example.swyp_team1_back.global.common.response;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<Response.ErrorDetail> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new Response.ErrorDetail(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return ResponseUtil.createValidationErrorResponse("유효성 검사에 실패하였습니다.", errors);
    }

    @ExceptionHandler(CustomFieldException.class)
    public ResponseEntity<Response<Void>> handleSignUpFieldException(CustomFieldException ex) {  // CustomFieldException
        String message;
        if (ex.getErrorCode() == ErrorCode.INVALID_PASSWORD || ex.getErrorCode() == ErrorCode.EMAIL_NOT_FOUND) {
            message = "로그인에 실패하였습니다.";
        } else {
            message = "회원가입에 실패하였습니다.";
        }
        return ResponseUtil.createExceptionResponse(message, ex.getErrorCode(), ex.getField(), ex.getMessage());
    }
}
