package com.kr.formdang.controller;

import com.kr.formdang.exception.CustomException;
import com.kr.formdang.dto.GlobalCode;
import com.kr.formdang.dto.DefaultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionController {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<DefaultResponse> handle(MethodArgumentNotValidException e) { // 파라미터 오류 글로벌 처리
        log.error("[MethodArgumentNotValidException]");
        DefaultResponse response = new DefaultResponse(GlobalCode.PARAMETER_ERROR);
        response.setErrorMsg(e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<DefaultResponse> handle(BindException e) { // 파라미터 바인딩 오류 글로벌 처리
        log.error("[BindException]");
        DefaultResponse response = new DefaultResponse(GlobalCode.BIND_ERROR);
        response.setErrorMsg(e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage());
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<DefaultResponse> handle(HttpMessageNotReadableException e) { // 자료형 오류 글로벌 처리
        log.error("[HttpMessageNotReadableException]", e);
        return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.HTTP_MESSAGE_NOT_READABLE_ERROR));
    }

    @ExceptionHandler(value = MissingRequestHeaderException.class)
    public ResponseEntity<DefaultResponse> handle(MissingRequestHeaderException e) { // 헤더 누락 오류 글로벌 처리
        log.error("[MissingRequestHeaderException]");
        return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.NOT_EXIST_HEADER));
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<DefaultResponse> handle(MissingServletRequestParameterException e) { // 파라미터 누락 오류 글로벌 처리
        log.error("[MissingServletRequestParameterException]");
        return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.NOT_EXIST_PARAM));
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<DefaultResponse> handle(MethodArgumentTypeMismatchException e) { // 파라미터 검증 오류 글로벌 처리
        log.error("[MethodArgumentTypeMismatchException]");
        return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.BIND_ERROR));
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<DefaultResponse> handle(IOException e) { // IO Exception 글로벌 처리
        log.error("[IOException]: ", e);
        return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.IO_EXCEPTION));
    }

    @ExceptionHandler(value = RestClientException.class)
    public ResponseEntity<DefaultResponse> handle(RestClientException e) { // RestClientException 글로벌 처리
        log.error("[RestClientException]: ", e);
        return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.NETWORK_ERROR));
    }

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<DefaultResponse> handle(CustomException e) { // 커스텀 오류 글로벌 처리
        log.error("[CustomException]: {}", e.getMessage());
        return ResponseEntity.ok().body(new DefaultResponse(e.getCode()));
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<DefaultResponse> handle(RuntimeException e) { // 런타임 오류 글로벌 처리
        log.error("[RuntimeException]", e);
        return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.SYSTEM_ERROR));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<DefaultResponse> handle404(NoHandlerFoundException e) { // 404 오류 글로벌 처리
        return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.NOT_FOUND_PAGE));
    }


}
