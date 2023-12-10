package com.kr.formdang.controller;

import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.entity.QuestionTbEntity;
import com.kr.formdang.exception.CustomException;
import com.kr.formdang.jwt.JwtService;
import com.kr.formdang.model.common.GlobalCode;
import com.kr.formdang.model.layer.FormDataDto;
import com.kr.formdang.model.layer.QuestionDataDto;
import com.kr.formdang.model.net.request.FormSubmitRequest;
import com.kr.formdang.model.root.DefaultResponse;
import com.kr.formdang.service.form.FormDataService;
import com.kr.formdang.service.form.FormService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FormController {

    private final FormService formService;
    private final FormDataService formDataService;
    private final JwtService jwtService;


    @PostMapping(value = "/form/submit")
    public ResponseEntity submitForm(@Valid @RequestBody FormSubmitRequest request, @RequestHeader("Authorization") String token) {
        try {
            FormDataDto formDataDto = new FormDataDto(request, "yyyyMMdd"); // 폼 데이터 생성
            formDataDto.setAid(jwtService.getId(token)); // 관리자 아이디 세팅
            FormTbEntity formTb = formService.submitForm(formDataService.getFormData(formDataDto),
                    request.getQuestion().stream().map(it -> formDataService.getQuestionData(new QuestionDataDto(it))).collect(Collectors.toList())); // 폼 저장
            if (formTb != null && formTb.getFid() != null) {
                return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.SUCCESS));
            } else {
                return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.FAIL_SUBMIT_FORM));
            }
        } catch (CustomException e) {
            return ResponseEntity.ok().body(new DefaultResponse(e.getCode()));
        } catch (Exception e) {
            log.error("{}", e);
            return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.SYSTEM_ERROR));
        }
    }

}
