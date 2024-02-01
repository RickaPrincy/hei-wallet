package com.hei.wallet.heiwallet.exception;

import org.springframework.http.HttpStatus;

import java.util.Objects;

public class ApiErrorResponse {
    private final String message;
    private final int code;
    public ApiErrorResponse(String message, HttpStatus status) {
        this.message = message;
        this.code = status.value();
    }

    public ApiErrorResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiErrorResponse that = (ApiErrorResponse) o;
        return code == that.code && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, code);
    }
}
