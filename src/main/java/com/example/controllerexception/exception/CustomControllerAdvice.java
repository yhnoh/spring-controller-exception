package com.example.controllerexception.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//ResponseEntityExceptionHandler
@RestControllerAdvice
@Slf4j
public class CustomControllerAdvice {

    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<CustomExceptionResponse> customException(CustomException e){
        log.error("error_code = {}, error_msg = {}", e.getError_code(), e.getMsg());
        return CustomExceptionResponse.responseEntity(HttpStatus.NOT_FOUND, e.getError_code(), e.getMsg());
    }

    @ExceptionHandler(value = IllegalStateException.class)
    private ResponseEntity illegalStateException(IllegalStateException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("전역 에러");
    }


}
