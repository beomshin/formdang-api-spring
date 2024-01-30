package com.kr.formdang.controller;

import com.kr.formdang.entity.AdminSubTbEntity;
import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.entity.QuestionTbEntity;
import com.kr.formdang.exception.CustomException;
import com.kr.formdang.jwt.JwtService;
import com.kr.formdang.model.common.GlobalCode;
import com.kr.formdang.model.layer.FormDataDto;
import com.kr.formdang.model.layer.FormFindDto;
import com.kr.formdang.model.layer.QuestionDataDto;
import com.kr.formdang.model.net.request.FormSubmitRequest;
import com.kr.formdang.model.net.response.AnalyzeFormResponse;
import com.kr.formdang.model.net.response.FindFormResponse;
import com.kr.formdang.model.root.DefaultResponse;
import com.kr.formdang.service.form.FormDataService;
import com.kr.formdang.service.form.FormService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FormController {

    private final FormService formService;
    private final FormDataService formDataService;
    private final JwtService jwtService;


    /**
     * 폼 등록 API
     *
     * 폼 작성하기 API
     * 2024-01-01 현 버전 퀴즈 등록만 가능
     *
     * @param request
     * @param token
     * @return
     */
    @PostMapping(value = "/form/submit")
    public ResponseEntity submitForm(
            @Valid @RequestBody FormSubmitRequest request,
            @RequestHeader("Authorization") String token)
    {
        try {
            final String pattern = "yyyyMMdd";
            final Long aid = jwtService.getId(token); // 관리자 아이디 세팅

            FormTbEntity formTbEntity = formDataService.getFormData(new FormDataDto(request, aid, pattern)); // 폼 엔티티 생성

            List<QuestionTbEntity> questionTbEntities = request.getQuestion().stream()
                    .map(it -> formDataService.getQuestionData(new QuestionDataDto(it)))
                    .collect(Collectors.toList()); // 질문 엔티티 리스트 생성

            FormTbEntity formTb = formService.submitForm(formTbEntity, questionTbEntities); // 폼 저장

            GlobalCode resCode = formTb != null && formTb.getFid() != null ? GlobalCode.SUCCESS :  GlobalCode.FAIL_SUBMIT_FORM; // 폼 성공 여부 코드 저장

            return ResponseEntity.ok().body(new DefaultResponse(resCode));

        } catch (CustomException e) {
            log.error("[폼 등록 API][/form/submit] - {}", e.getCode());
            return ResponseEntity.ok().body(new DefaultResponse(e.getCode()));
        } catch (Exception e) {
            log.error("[폼 등록 API][/form/submit] - {}", e);
            return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.SYSTEM_ERROR));
        }
    }

    /**
     * 폼 리스트 조회 API
     *
     * 폼 리스트 조회
     * 페이징 처리 ( 0~ )
     * 타입 처리 ( 설문, 퀴즈 .. ) 2024-01-01 현 기능 사용안함
     * 상태 처리 ( 진행, 종료, 임시 )
     *
     * @param page
     * @param type
     * @param status
     * @param token
     * @return
     */
    @GetMapping(value = "/form/find")
    public ResponseEntity findForm(
            @RequestParam @NotBlank @Min(value = 0, message = "페이지 개수는 0이상입니다.") Integer page,
            @RequestParam @NotBlank @Min(0) Integer type,
            @RequestParam @NotBlank @Min(0) Integer status,
            @RequestParam @NotBlank @Min(0) Integer order,
            @RequestHeader("Authorization") String token)
    {
        try {
            log.info("■ 1. 폼리스트 조회 시작");
            final Long aid = jwtService.getId(token); // 관리자 아이디 세팅
            Page pages = formService.findForm(new FormFindDto(page, type, aid, status, order)); // 폼 리스트 조회
            AdminSubTbEntity adminSubTb = formService.analyzeForm(aid); // 종합 정보 조회
            log.info("■ 4. 폼리스트 조회 응답 성공");
            return ResponseEntity.ok().body(new FindFormResponse(pages, adminSubTb));
        } catch (CustomException e) {
            log.info("■ 4. 폼리스트 조회 응답 실패");
            log.error("[폼 리스트 조회 API][/form/find] - {}", e.getCode());
            return ResponseEntity.ok().body(new DefaultResponse(e.getCode()));
        } catch (Exception e) {
            log.info("■ 4. 폼리스트 조회 응답 실패");
            log.error("[폼 리스트 조회 API][/form/find] - {}", e);
            return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.SYSTEM_ERROR));
        }
    }

    /**
     * 종합 분석 조회하기 API
     *
     * 종합 분석 데이터 조회
     * 퀴즈, 설문 생성 개수, 응답 개수
     *
     * 2023-01-01 현 버전 사용안함 (해당 정보 find에서 함께 전달 처리 )
     *
     * @param token
     * @return
     */
    @GetMapping(value = "/form/analyze")
    public ResponseEntity analyzeForm(@RequestHeader("Authorization") String token) {
        try {
            final Long aid = jwtService.getId(token); // 관리자 아이디 세팅
            AdminSubTbEntity adminSubTb = formService.analyzeForm(aid); // 종합 정보 조회
            return ResponseEntity.ok().body(new AnalyzeFormResponse(adminSubTb));
        } catch (CustomException e) {
            log.error("[종합 분석 조회하기 API][/form/analyze] - {}", e.getCode());
            return ResponseEntity.ok().body(new DefaultResponse(e.getCode()));
        } catch (Exception e) {
            log.error("[종합 분석 조회하기 API][/form/analyze] - {}", e);
            return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.SYSTEM_ERROR));
        }
    }

}
