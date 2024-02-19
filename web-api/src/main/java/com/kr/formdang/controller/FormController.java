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
import com.kr.formdang.model.net.request.FormUpdateRequest;
import com.kr.formdang.model.net.response.*;
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
import java.util.Comparator;
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
            log.info("■ 1. 폼 작성하기 요청 성공");
            final String pattern = "yyyyMMdd";
            final Long aid = jwtService.getId(token); // 관리자 아이디 세팅

            FormTbEntity formTbEntity = formDataService.getFormData(new FormDataDto(request, aid, pattern)); // 폼 엔티티 생성

            List<QuestionTbEntity> questionTbEntities = request.getQuestion().stream()
                    .map(it -> formDataService.getQuestionData(new QuestionDataDto(it)))
                    .collect(Collectors.toList()); // 질문 엔티티 리스트 생성

            FormTbEntity formTb = formService.submitForm(formTbEntity, questionTbEntities); // 폼 저장

            GlobalCode resCode = formTb != null && formTb.getFid() != null ? GlobalCode.SUCCESS :  GlobalCode.FAIL_SUBMIT_FORM; // 폼 성공 여부 코드 저장

            log.info("■ 5. 폼 작성하기 응답 성공");
            return ResponseEntity.ok().body(new SubmitFormResponse(resCode, formTb != null ? formTb.getFid() : null));
        } catch (CustomException e) {
            log.error("■ 5. 폼 작성하기 응답 오류, {}", e);
            return ResponseEntity.ok().body(new DefaultResponse(e.getCode()));
        } catch (Exception e) {
            log.error("■ 5. 폼 작성하기 응답 오류, {}", e);
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
    @GetMapping(value = "/form/list/find")
    public ResponseEntity findFormList(
            @RequestParam @NotBlank @Min(value = 0, message = "페이지 개수는 0이상입니다.") Integer page,
            @RequestParam @NotBlank @Min(0) Integer type,
            @RequestParam @NotBlank @Min(0) Integer status,
            @RequestParam @NotBlank @Min(0) Integer order,
            @RequestHeader("Authorization") String token)
    {
        try {
            log.info("■ 1. 폼리스트 조회 요청 성공");
            final Long aid = jwtService.getId(token); // 관리자 아이디 세팅
            Page pages = formService.findFormList(new FormFindDto(page, type, aid, status, order)); // 폼 리스트 조회
            AdminSubTbEntity adminSubTb = formService.analyzeForm(aid); // 종합 정보 조회
            log.info("■ 4. 폼리스트 조회 응답 성공");
            return ResponseEntity.ok().body(new FindFormListResponse(pages, adminSubTb));
        } catch (CustomException e) {
            log.error("■ 4. 폼리스트 조회 응답 오류, {}", e);
            return ResponseEntity.ok().body(new DefaultResponse(e.getCode()));
        } catch (Exception e) {
            log.error("■ 4. 폼리스트 조회 응답 오류, {}", e);
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
            log.info("■ 1. 종합 분석 조회 요청 성공");
            final Long aid = jwtService.getId(token); // 관리자 아이디 세팅
            log.info("■ 2. 종합 분석 로그 단게 맞추기");
            AdminSubTbEntity adminSubTb = formService.analyzeForm(aid); // 종합 정보 조회
            log.info("■ 4. 종합 분석 조회 응답 성공");
            return ResponseEntity.ok().body(new AnalyzeFormResponse(adminSubTb));
        } catch (CustomException e) {
            log.error("■ 종합 분석 조회 응답 오류, {}", e);
            return ResponseEntity.ok().body(new DefaultResponse(e.getCode()));
        } catch (Exception e) {
            log.error("■ 종합 분석 조회 응답 오류, {}", e);
            return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.SYSTEM_ERROR));
        }
    }

    /**
     * 폼 상세 정보 조회하기
     *
     * @param token
     * @param fid
     * @return
     */
    @GetMapping(value = "/form/detail/{fid}/find")
    public ResponseEntity findFormDetail(@RequestHeader("Authorization") String token, @PathVariable("fid") Long fid) {
        try {
            log.info("■ 1. 폼 상세 정보 조회 요청 성공");
            final Long aid = jwtService.getId(token); // 관리자 아이디 세팅
            FormTbEntity formTbEntity = formService.findForm(aid, fid); // 폼 상세 조회
            List<QuestionTbEntity> questionTbEntities = formService.findQuestions(fid); // 질문 리스트 조회
            log.info("■ 4. 폼 상세 정보 조회 응답 성공");
            return ResponseEntity.ok().body(new FindFormDetailResponse(formTbEntity, questionTbEntities));
        } catch (CustomException e) {
            log.error("■ 폼 상세 정보 조회 응답 오류, {}", e);
            return ResponseEntity.ok().body(new DefaultResponse(e.getCode()));
        } catch (Exception e) {
            log.error("■ 폼 상세 정보 조회 응답 오류, {}", e);
            return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.SYSTEM_ERROR));
        }
    }


    @PostMapping(value = "/form/{fid}/update")
    public ResponseEntity updateForm(@RequestHeader("Authorization") String token, @PathVariable("fid") Long fid,
                                     @Valid @RequestBody FormUpdateRequest request) {
        try {
            final String pattern = "yyyyMMdd";
            log.info("■ 1. 폼 상세 정보 조회 요청 성공");
            final Long aid = jwtService.getId(token); // 관리자 아이디 세팅
            FormDataDto formDataDto = new FormDataDto(request, fid, aid, pattern); // 폼 데이터
            List<QuestionDataDto> questionDataDtos = request.getQuestion().stream().map(it -> new QuestionDataDto(it)).sorted(Comparator.comparing(QuestionDataDto::getOrder)).collect(Collectors.toList()); // 질문 데이터 order 순 정렬
            formService.updateForm(formDataDto, questionDataDtos); // 업데이트 처리
            return ResponseEntity.ok().body(new DefaultResponse());
        } catch (CustomException e) {
            log.error("■ 폼 상세 정보 조회 응답 오류, {}", e);
            return ResponseEntity.ok().body(new DefaultResponse(e.getCode()));
        } catch (Exception e) {
            log.error("■ 폼 상세 정보 조회 응답 오류, {}", e);
            return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.SYSTEM_ERROR));
        }
    }


    /**
     * 유저 화면 데이터 제공
     *
     * @param token
     * @param fid
     * @return
     */
    @GetMapping(value = "/public/form/paper")
    public ResponseEntity findPaper(@RequestHeader(value = "Authorization", required = false) String token,
                                    @RequestParam @NotBlank @Min(0) Integer type,
                                    @RequestParam @NotBlank @Min(0) Long fid,
                                    @RequestParam @NotBlank String key) {
        try {
            log.info("■ 1. 유저 화면 정보 조회 요청 성공");
            FormTbEntity formTbEntity = formService.findPaper(new FormDataDto(fid, type, key, token)); // 폼 상세 조회
            List<QuestionTbEntity> questionTbEntities = formService.findQuestions(fid); // 질문 리스트 조회
            log.info("■ 4. 유저 화면 정보 조회 응답 성공");
            return ResponseEntity.ok().body(new FindPaperResponse(formTbEntity, questionTbEntities));
        } catch (CustomException e) {
            log.error("■ 유저 화면 정보 조회 응답 오류, {}", e);
            return ResponseEntity.ok().body(new DefaultResponse(e.getCode()));
        } catch (Exception e) {
            log.error("■ 유저 화면 정보 조회 응답 오류, {}", e);
            return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.SYSTEM_ERROR));
        }
    }



}
