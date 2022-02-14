package com.example.controllerexception.exception;

public class CustomChildException extends CustomException{

    public CustomChildException() {
        super();
    }

    public CustomChildException(String message) {
        super(message);
    }

    public CustomChildException(String error_code, String message) {
        super(error_code, message);
    }
}
