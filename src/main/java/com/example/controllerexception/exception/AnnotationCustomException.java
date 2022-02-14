package com.example.controllerexception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 어노테이션을 이용한 response
 * 1.exception이 발생하면 status와 message를 전달
 *
 * 장점
 * 간단하다.
 *
 * 단점
 * 동일 status 동일 message 전달달
 */

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "AnnotationCustomException 입니다.")
public class AnnotationCustomException extends RuntimeException{

    public AnnotationCustomException(String message) {
        super(message);
    }
}
