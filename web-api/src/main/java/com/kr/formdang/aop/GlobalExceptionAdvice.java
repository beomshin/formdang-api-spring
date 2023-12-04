package com.kr.formdang.aop;

import com.kr.formdang.exception.CustomException;
import com.kr.formdang.model.common.GlobalCode;
import com.kr.formdang.model.root.DefaultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity<DefaultResponse> handle(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidException]", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DefaultResponse(GlobalCode.PARAMETER_ERROR));
    }

    @ExceptionHandler
    public ResponseEntity<DefaultResponse> handle(BindException e) {
        log.error("[BindException]", e);
        return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.BIND_ERROR));
    }

    @ExceptionHandler
    public ResponseEntity<DefaultResponse> handle(HttpMessageNotReadableException e) {
        log.error("[HttpMessageNotReadableException]", e);
        return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.HTTP_MESSAGE_NOT_READABLE_ERROR));
    }

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<DefaultResponse> handle(CustomException e) {
        log.error("[CustomException]");
        return ResponseEntity.ok().body(new DefaultResponse(e.getCode()));
    }

    @ExceptionHandler
    public ResponseEntity<DefaultResponse> handle(RuntimeException e) {
        log.error("[RuntimeException]", e);
        return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.SYSTEM_ERROR));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<DefaultResponse> handle404(NoHandlerFoundException e) {
        return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.NOT_FOUND_PAGE));
    }


}
