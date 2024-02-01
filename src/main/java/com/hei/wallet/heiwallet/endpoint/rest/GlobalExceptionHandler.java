package com.hei.wallet.heiwallet.endpoint.rest;

import com.hei.wallet.heiwallet.exception.ApiErrorResponse;
import com.hei.wallet.heiwallet.exception.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ApiException.class})
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(ApiException error) {
        return new ResponseEntity<>(
                new ApiErrorResponse(error.getMessage(), error.getStatus()),
                error.getStatus()
        );
    }
}
