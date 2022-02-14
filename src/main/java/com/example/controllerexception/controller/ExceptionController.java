package com.example.controllerexception.controller;

import com.example.controllerexception.exception.AnnotationCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
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
    public void Exception(){
        throw new AnnotationCustomException("RuntimeException 에러 발생");
    }


    /**
     *
     */
    @ExceptionHandler(value = )


}
