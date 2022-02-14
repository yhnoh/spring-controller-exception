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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
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
 * https://bcp0109.tistory.com/303
 *
 * 1.@ResponseStatus
 *
 * 2.ResponseStatusException : @ResponseStatus의 대체제
 * HandlerExceptionResolver가 모든 exception 을 가로채서 처리, 구현체 ResponseStatusExceptionResolver
 */
@WebMvcTest(controllers = ExceptionController.class)
public class MemberControllerTest {


    @Autowired
    private MockMvc mvc;

    @Test
    public void exceptionTest() throws Exception{
        Assertions.assertThatThrownBy(() -> mvc.perform(MockMvcRequestBuilders.get("/exception"))
                .andDo(MockMvcResultHandlers.print())).isInstanceOf(Exception.class);
    }

    @Test
    public void runtimeExceptionTest() throws Exception{
        Assertions.assertThatThrownBy(() -> mvc.perform(MockMvcRequestBuilders.get("/runtime-exception"))
                .andDo(MockMvcResultHandlers.print())).isInstanceOf(NestedServletException.class);
    }

    @Test
    public void annotationCustomException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/annotation-custom-exception"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }


//    @Before
//    public void before(){
//        objectMapper = new ObjectMapper();
//    }
//
//
//
//    @Test
//    public void 회원가입() throws Exception{
//        //given
//        MemberJoin memberJoin = new MemberJoin("username", "password");
//        String json = objectMapper.writeValueAsString(memberJoin);
//
//        BDDMockito
//                .given(memberService.join(memberJoin.getUsername(), memberJoin.getPassword()))
//                .willReturn(new Member("username", "password"));
//
//
//        //when
//        ResultActions actions = mvc.perform(MockMvcRequestBuilders.post("/members")
//                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8)
//                .content(json))
//                .andDo(MockMvcResultHandlers.print());
//
//        //then
//        actions
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                //.andExpect(MockMvcResultMatchers.jsonPath("username", Is.is("username")));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Is.is("username")));
//
//    }
//
//    @Test
//    public void 회원가입_중복예외처리() throws Exception{
//        //given
//        MemberJoin memberJoin = new MemberJoin("username", "password");
//
//        String json = objectMapper.writeValueAsString(memberJoin);
//
//        memberService.join(memberJoin.getUsername(), memberJoin.getPassword());
//        BDDMockito
//                .given(memberService.join(memberJoin.getUsername(), memberJoin.getPassword()))
//                .willReturn(new Member("username", "password"));
//
//        //when
//        ResultActions actions = mvc.perform(MockMvcRequestBuilders.post("/members")
//                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8)
//                .content(json))
//                .andDo(MockMvcResultHandlers.print());
//
//        //then
//        actions
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                //.andExpect(MockMvcResultMatchers.jsonPath("username", Is.is("username")));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Is.is("username")));
//
//
//
//    }
}
