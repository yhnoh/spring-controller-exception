package com.example.controllerexception.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    String error_code = "";
    String msg = "";

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String error_code, String message) {
        super(message);
        this.error_code = error_code;
    }


}
