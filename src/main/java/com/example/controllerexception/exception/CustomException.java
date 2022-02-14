package com.example.controllerexception.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
public class CustomException extends RuntimeException{
    String error_code = "";
    String msg = "";

    public CustomException() {
        super();
    }

    public CustomException(String message) {
        super(message);
        this.msg = message;
    }

    public CustomException(String error_code, String message) {
        super(message);
        this.error_code = error_code;
        this.msg = message;
    }


}
