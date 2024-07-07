package com.example.swyp_team1_back.global.common.response;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ApiResponse.ErrorDetail> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ApiResponse.ErrorDetail(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return ApiResponseUtil.createValidationErrorResponse("유효성 검사에 실패하였습니다.", errors);
    }

    @ExceptionHandler(CustomFieldException.class)
    public ResponseEntity<ApiResponse<Void>> handleSignUpFieldException(CustomFieldException ex) {  // CustomFieldException
        return ApiResponseUtil.createExceptionResponse("회원가입에 실패하였습니다.", ErrorCode.ILLEGAL_ARGUMENT_ERROR, ex.getField(), ex.getMessage());
    }

}
