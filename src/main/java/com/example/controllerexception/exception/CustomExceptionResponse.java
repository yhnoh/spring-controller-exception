package com.example.controllerexception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class CustomExceptionResponse {

    private LocalDateTime now = LocalDateTime.now();
    private boolean result = false;
    private String error_code;
    private String msg;


    public static ResponseEntity<CustomExceptionResponse> responseEntity(HttpStatus status, String error_code, String msg){
        CustomExceptionResponse customExceptionResponse = new CustomExceptionResponse();
        customExceptionResponse.error_code = error_code;
        customExceptionResponse.msg = msg;

        return ResponseEntity.status(status).body(customExceptionResponse);
    }
}
