package com.example.controllerexception.controller;

import com.example.controllerexception.exception.AnnotationCustomException;
import com.example.controllerexception.exception.CustomChildException;
import com.example.controllerexception.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
public class ExceptionController {

    @GetMapping(value = "/exception")
    public void exception() throws Exception{
        throw new Exception("Exception 에러 발생");
    }

    @GetMapping(value = "/runtime-exception")
    public void runtimeException() throws RuntimeException{
        throw new RuntimeException("RuntimeException 에러 발생");
    }

    @GetMapping(value = "/annotation-custom-exception")
    public void annotationCustomException(){
        throw new AnnotationCustomException("RuntimeException 에러 발생");
    }

    @GetMapping(value = "/exception-handler")
    public void exceptionHandler(){
        throw new IllegalArgumentException("IllegalArgumentException 에러 발생");
    }

    @GetMapping(value = "/controller-advice")
    private void controllerAdvice(){
        throw new CustomException("에러코드", "에러메시지");
    }

    @GetMapping(value = "/polymorphism-controller-advice")
    private void polymorphismControllerAdvice(){
        throw new CustomChildException("자식 에러코드", "자식 에러메시지");
    }

    @GetMapping(value = "/duplication-exception-handler")
    private void duplicationExceptionHandler(){
        throw new IllegalStateException();
    }

    /**
     *  해당 에러를 인자로 받을 수있다.
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    private ResponseEntity illegalArgumentExceptionHandler(IllegalArgumentException e){
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(value = IllegalStateException.class)
    private ResponseEntity illegalStateException(IllegalStateException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("클래스 에러");
    }





}
