package com.kr.formdang.aop;

import com.kr.formdang.exception.FormException;
import com.kr.formdang.exception.FormHttpException;
import com.kr.formdang.exception.FormLoginException;
import com.kr.formdang.exception.ResultCode;
import com.kr.formdang.model.response.AbstractResponse;
import com.kr.formdang.model.response.FailResponse;
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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionController {


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<AbstractResponse> handle(MethodArgumentNotValidException e) { // 파라미터 오류 글로벌 처리
        log.error("■ 응답 오류 [MethodArgumentNotValidException]");
        AbstractResponse response = new FailResponse(ResultCode.PARAMETER_ERROR);
        response.setErrorMsg(e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<AbstractResponse> handle(BindException e) { // 파라미터 바인딩 오류 글로벌 처리
        log.error("■ 응답 오류 [BindException]");
        AbstractResponse response = new FailResponse(ResultCode.BIND_ERROR);
        response.setErrorMsg(e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage());
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<AbstractResponse> handle(HttpMessageNotReadableException e) { // 자료형 오류 글로벌 처리
        log.error("■ 응답 오류 [HttpMessageNotReadableException]", e);
        return ResponseEntity.ok().body(new FailResponse(ResultCode.HTTP_MESSAGE_NOT_READABLE_ERROR));
    }

    @ExceptionHandler(value = MissingRequestHeaderException.class)
    public ResponseEntity<AbstractResponse> handle(MissingRequestHeaderException e) { // 헤더 누락 오류 글로벌 처리
        log.error("■ 응답 오류 [MissingRequestHeaderException]");
        return ResponseEntity.ok().body(new FailResponse(ResultCode.NOT_EXIST_HEADER));
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<AbstractResponse> handle(MissingServletRequestParameterException e) { // 파라미터 누락 오류 글로벌 처리
        log.error("■ 응답 오류 [MissingServletRequestParameterException]");
        return ResponseEntity.ok().body(new FailResponse(ResultCode.NOT_EXIST_PARAM));
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<AbstractResponse> handle(MethodArgumentTypeMismatchException e) { // 파라미터 검증 오류 글로벌 처리
        log.error("■ 응답 오류 [MethodArgumentTypeMismatchException]");
        return ResponseEntity.ok().body(new FailResponse(ResultCode.BIND_ERROR));
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<AbstractResponse> handle(IOException e) { // IO Exception 글로벌 처리
        log.error("■ 응답 오류 [IOException]: ", e);
        return ResponseEntity.ok().body(new FailResponse(ResultCode.IO_EXCEPTION));
    }

    @ExceptionHandler(value = FormHttpException.class)
    public ResponseEntity<AbstractResponse> handle(FormHttpException e) { // FormHttpException 글로벌 처리
        log.error("■ 응답 오류 [RestClientException]: ", e);
        return ResponseEntity.ok().body(new FailResponse(e.getCode()));
    }

    @ExceptionHandler(value = FormException.class)
    public ResponseEntity<AbstractResponse> handle(FormException e) { // 커스텀 오류 글로벌 처리
        log.error("■ 응답 오류 [CustomException]: {}", e.getMessage());
        return ResponseEntity.ok().body(new FailResponse(e.getCode()));
    }

    @ExceptionHandler(value = FormLoginException.class)
    public RedirectView handle(FormLoginException e) { // 커스텀 오류 글로벌 처리
        log.error("■ 로그인 오류 [LoginException]: {}", e.getMessage());
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(e.getUrl()); // 폼당폼당 로그인 실패 페이지 세팅
        return redirectView;
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<AbstractResponse> handle(Exception e) {
        log.error("■ 응답 오류 [Exception]", e);
        return ResponseEntity.ok().body(new FailResponse(ResultCode.SYSTEM_ERROR));
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<AbstractResponse> handle(RuntimeException e) { // 런타임 오류 글로벌 처리
        log.error("■ 응답 오류 [RuntimeException]", e);
        return ResponseEntity.ok().body(new FailResponse(ResultCode.SYSTEM_ERROR));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<AbstractResponse> handle404(NoHandlerFoundException e) { // 404 오류 글로벌 처리
        return ResponseEntity.ok().body(new FailResponse(ResultCode.NOT_FOUND_PAGE));
    }


}
