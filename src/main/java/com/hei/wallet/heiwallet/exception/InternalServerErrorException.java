package com.hei.wallet.heiwallet.exception;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends ApiException {
    public InternalServerErrorException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public InternalServerErrorException(){
        super("Internal Server Error", HttpStatus.BAD_REQUEST);
    }
}
