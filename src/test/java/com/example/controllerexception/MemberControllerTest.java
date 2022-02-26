package com.example.controllerexception;

import com.example.controllerexception.controller.ExceptionController;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;


@EqualsAndHashCode
@Slf4j
@RunWith(SpringRunner.class)
/**
 * @WebMvcTest 밑에 내용 만 스캔
 * @Controller, @ControllerAdvice, @JsonComponent, Converter, GenericConverter, Filter, HandlerInterceptor, WebMvcConfigurer, HandlerMethodArgumentResolver
 */
/**
 * 1.@ResponseStatus
 * 2.ResponseStatusException : @ResponseStatus의 대체제
 * HandlerExceptionResolver가 모든 exception 을 가로채서 처리, 구현체 ResponseStatusExceptionResolver
 *
 */
@WebMvcTest(controllers = ExceptionController.class)
public class MemberControllerTest {


    @Autowired
    private MockMvc mvc;

    //1. 일반 예외 발생시
    @Test
    public void exceptionTest() throws Exception{
        Assertions.assertThatThrownBy(() -> mvc.perform(MockMvcRequestBuilders.get("/exception"))
                .andDo(MockMvcResultHandlers.print())).isInstanceOf(Exception.class);
    }

    //2. 런타임 예외 발생시
    @Test
    public void runtimeExceptionTest() throws Exception{
        Assertions.assertThatThrownBy(() -> mvc.perform(MockMvcRequestBuilders.get("/runtime-exception"))
                .andDo(MockMvcResultHandlers.print())).isInstanceOf(NestedServletException.class);
    }

    //3. @ResponseStatus을 이용한 예외 처리
    @Test
    public void annotationCustomException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/annotation-custom-exception"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }


    //4. @ExceptionHandler를 Controller클래스 내에 있는 에러를 처리 2개 이상도 공통으로 처리 가능
    //Exception을 에러로 받을 수 있다.
    @Test
    public void exceptionHandler() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/exception-handler"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    //5. @RestControllerAdvice 를 이용하여 전역 처리하기
    //전역 처리를 위해서 CustomControllerAdvice 클래스 생성
    //RuntimeException 상속 받아서 CustomException 클래스 재정의
    //CustomException클래스 @ExceptionHandler로 에러 핸들링
    //CustomExceptionResponse을 이용하여 return 형식 동일화
    //실무에서 제일 쓸만하다.
    @Test
    public void controllerAdvice() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/controller-advice"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value("에러코드"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("에러메시지"))
                .andDo(MockMvcResultHandlers.print());
    }

    //6. 다형성을 이용하여 상속받은 자식도 동일 애러 핸들링이 가능한지 테스트
    //부모를 상속받은 에러도 동일한 @ExceptionHandler 처리가 가능
    @Test
    public void polymorphismControllerAdvice() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/polymorphism-controller-advice"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value("자식 에러코드"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("자식 에러메시지"))
                .andDo(MockMvcResultHandlers.print());
    }

    //컨트롤러 클래스 내에서 @ExceptionHandler가 존재하고, ControllerAdvice내에서도 존재하면 우선순위는 누구인가?
    //당연하게도 클래스쪽 에러가 나온다.
    @Test
    public void duplicationExceptionHandler() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/duplication-exception-handler"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("클래스 에러"))
                //.andExpect(MockMvcResultMatchers.content().string("전역 에러"))
                .andDo(MockMvcResultHandlers.print());

    }
}
