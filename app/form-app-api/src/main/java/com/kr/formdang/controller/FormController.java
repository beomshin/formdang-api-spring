package com.kr.formdang.controller;

import com.kr.formdang.dto.*;
import com.kr.formdang.dto.auth.AuthUser;
import com.kr.formdang.dto.auth.JwtTokenAuthUser;
import com.kr.formdang.dto.res.*;
import com.kr.formdang.entity.AdminSubTbEntity;
import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.entity.QuestionTbEntity;
import com.kr.formdang.enums.PageEnum;
import com.kr.formdang.exception.FormException;
import com.kr.formdang.exception.ResultCode;
import com.kr.formdang.dto.req.FormSubmitRequest;
import com.kr.formdang.dto.req.FormUpdateRequest;
import com.kr.formdang.service.form.FormService;
import com.kr.formdang.utils.FormFlagUtils;
import com.kr.formdang.utils.str.StrUtils;
import com.kr.formdang.utils.time.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FormController {

    private final FormService formService;

    /**
     * 폼 등록 API
     */
    @PostMapping(value = "/form/submit")
    public ResponseEntity<RootResponse> submitForm(
            @Valid @RequestBody FormSubmitRequest request,
            @RequestHeader("Authorization") String token
    ) throws ParseException {

        final String pattern = "yyyyMMdd";

        log.info("■ 1. 폼 작성하기 요청 성공");
        AuthUser authUser = new JwtTokenAuthUser(token);

        FormTbEntity formTbEntity = FormTbEntity.builder()
                .aid(authUser.getId())
                .title(request.getTitle())
                .formType(request.getType())
                .formDetail(request.getDetail())
                .beginDt(TimeUtils.getTimeStamp(request.getBeginDt(), pattern))
                .endDt(TimeUtils.getTimeStamp(request.getEndDt(), pattern))
                .maxRespondent(request.getMaxRespondent())
                .questionCount(request.getQuestionCount() == request.getQuestion().size() ? request.getQuestionCount() : request.getQuestion().size())
                .logoUrl(request.getLogoUrl())
                .themeUrl(request.getThemeUrl())
                .status(request.getStatus())
                .build(); // 폼 엔티티 생성

        List<QuestionTbEntity> questionTbEntities = request.getQuestion().stream()
                .map(it ->
                        QuestionTbEntity.builder()
                                .order(it.getOrder())
                                .questionType(it.getType())
                                .title(it.getTitle())
                                .questionPlaceholder(it.getPlaceholder())
                                .imageUrl(it.getImageUrl())
                                .count(it.getCount())
                                .questionDetail(StrUtils.joining(it.getDetail(), "|"))
                                .questionExampleDetail(StrUtils.joining(it.getExampleDetail(), "|"))
                                .quizAnswer(StrUtils.joining(it.getAnswer(), "|"))
                                .build()
                )
                .collect(Collectors.toList()); // 질문 엔티티 리스트 생성

        FormTbEntity formTb = formService.submitForm(formTbEntity, questionTbEntities); // 폼 저장

        boolean isInsert = formTb != null && formTb.getFid() != null; // 폼 성공 여부

        if (isInsert) {
            log.info("■ 5. 폼 작성하기 성공 응답");
            return ResponseEntity.ok().body(new SubmitFormResponse(formTb.getFid()));
        } else {
            log.info("■ 5. 폼 작성하기 실패 응답");
            return ResponseEntity.ok().body(new DefaultResponse(ResultCode.FAIL_SUBMIT_FORM));
        }
    }

    /**
     * 폼 리스트 조회 API
     * 페이징 처리 ( 0~ )
     * 타입 처리 ( 설문, 퀴즈 .. )
     * 상태 처리 ( 진행, 종료, 임시 )
     */
    @GetMapping(value = "/form/list/find")
    public ResponseEntity<RootResponse> findFormList(
            @RequestParam @NotBlank @Min(value = 0, message = "페이지 개수는 0이상입니다.") Integer page,
            @RequestParam @NotBlank @Min(0) Integer type,
            @RequestParam @NotBlank @Min(0) Integer status,
            @RequestParam @NotBlank @Min(0) Integer order,
            @RequestHeader("Authorization") String token)
    {

        log.info("■ 1. 폼리스트 조회 요청 성공");
        AuthUser authUser = new JwtTokenAuthUser(token);

        PageRequest pageRequest = PageRequest.of(page, PageEnum.PAGE_12.getNum()); // 페이징 처리 생성
        SqlFormParam sqlFormParam = SqlFormParam.builder() // 조회 파라미터 생성
                .aid(authUser.getId())
                .offset(pageRequest.getOffset())
                .pageSize(pageRequest.getPageSize())
                .type(FormFlagUtils.type(type))
                .delFlag(FormFlagUtils.delFlag(status))
                .endFlag(FormFlagUtils.endFlag(status))
                .status(FormFlagUtils.status(status))
                .order(FormFlagUtils.order(order))
                .build();

        log.info("■ 2. 폼 리스트 조회 쿼리 시작");
        Page<FormTbDto> pages = formService.findFormList(sqlFormParam, pageRequest); // 폼 리스트 조회

        log.info("■ 3. 종합 분석 정보 조회 쿼리 시작");
        AdminSubTbEntity adminSubTb = formService.analyzeForm(authUser.getId()); // 종합 정보 조회

        DefaultResponse response = FindFormListResponse.builder()
                .totalElements(pages.getTotalElements())
                .totalPage(pages.getTotalPages())
                .curPage(pages.getNumber())
                .analyze(FindFormListResponse.Analyze
                        .builder()
                            .inspectionCnt(adminSubTb.getInspectionCnt())
                            .quizCnt(adminSubTb.getQuizCnt())
                            .inspectionRespondentCnt(adminSubTb.getInspectionRespondentCnt())
                            .quizRespondentCnt(adminSubTb.getQuizRespondent_cnt())
                        .build())
                .list(pages.getContent().stream().map(it -> FindFormListResponse.Forms
                                .builder()
                                    .fid(it.getFid())
                                    .type(it.getFormType())
                                    .title(it.getTitle())
                                    .logoUrl(it.getLogoUrl())
                                    .status(it.getStatus())
                                    .endFlag(it.getEndFlag())
                                    .delFlag(it.getDelFlag())
                                    .regDt(it.getRegDt())
                                .build())
                        .collect(Collectors.toList()))
                .build(); // 응답값 생성

        return ResponseEntity.ok().body(response);
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
    public ResponseEntity<RootResponse> analyzeForm(@RequestHeader("Authorization") String token) {
        try {
            log.info("■ 1. 종합 분석 조회 요청 성공");
            AuthUser authUser = new JwtTokenAuthUser(token);
            log.info("■ 2. 종합 분석 정보 조회 쿼리 시작");
            AdminSubTbEntity adminSubTb = formService.analyzeForm(authUser.getId()); // 종합 정보 조회
            log.info("■ 4. 종합 분석 조회 응답 성공");
            return ResponseEntity.ok().body(AnalyzeFormResponse
                    .builder()
                            .inspectionCnt(adminSubTb.getInspectionCnt())
                            .quizCnt(adminSubTb.getQuizCnt())
                            .inspectionRespondentCnt(adminSubTb.getInspectionRespondentCnt())
                            .quizRespondentCnt(adminSubTb.getQuizRespondent_cnt())
                    .build());
        } catch (Exception e) {
            log.error("■ 종합 분석 조회 응답 오류", e);
            return ResponseEntity.ok().body(new DefaultResponse(ResultCode.SYSTEM_ERROR));
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
    public ResponseEntity<RootResponse> findFormDetail(@RequestHeader("Authorization") String token, @PathVariable("fid") Long fid) {
        try {
            log.info("■ 1. 폼 상세 정보 조회 요청 성공");
            AuthUser authUser = new JwtTokenAuthUser(token);

            log.info("■ 2. 폼 상세 정보 조회 쿼리 시작");
            FormTbEntity formTbEntity = formService.findForm(authUser.getId(), fid); // 폼 상세 조회
            List<QuestionTbEntity> questionTbEntities = formService.findQuestions(fid); // 질문 리스트 조회
            final String separator = "\\|";
            List<FindFormDetailResponse.FormDetailQuestionResponse> question = questionTbEntities.stream()
                    .map(it ->
                            FindFormDetailResponse.FormDetailQuestionResponse
                                    .builder()
                                    .qid(it.getQid())
                                    .type(it.getQuestionType())
                                    .order(it.getOrder())
                                    .title(it.getTitle())
                                    .placeholder(it.getQuestionPlaceholder())
                                    .imageUrl(it.getImageUrl())
                                    .detail(StringUtils.isNotBlank(it.getQuestionDetail()) ? it.getQuestionDetail().split(separator) : null)
                                    .exampleDetail(StringUtils.isNotBlank(it.getQuestionExampleDetail()) ? it.getQuestionExampleDetail().split(separator) : null)
                                    .count(it.getCount())
                                    .answer(StringUtils.isNotBlank(it.getQuizAnswer()) ? it.getQuizAnswer().split(separator) : null)
                                    .build())
                    .collect(Collectors.toList());
            log.info("■ 4. 폼 상세 정보 조회 응답 성공");
            return ResponseEntity.ok().body(FindFormDetailResponse.builder()
                    .fid(formTbEntity.getFid())
                    .type(formTbEntity.getFormType())
                    .title(formTbEntity.getTitle())
                    .detail(formTbEntity.getFormDetail())
                    .beginDt(formTbEntity.getBeginDt())
                    .endDt(formTbEntity.getEndDt())
                    .questionCount(formTbEntity.getQuestionCount())
                    .status(formTbEntity.getStatus())
                    .endFlag(formTbEntity.getEndFlag())
                    .delFlag(formTbEntity.getDelFlag())
                    .answerCount(formTbEntity.getAnswerCount())
                    .maxRespondent(formTbEntity.getMaxRespondent())
                    .logoUrl(formTbEntity.getLogoUrl())
                    .themeUrl(formTbEntity.getThemeUrl())
                    .question(question)
                    .build());
        } catch (FormException e) {
            log.error("■ 폼 상세 정보 조회 응답 오류", e);
            return ResponseEntity.ok().body(new DefaultResponse(e.getCode()));
        } catch (Exception e) {
            log.error("■ 폼 상세 정보 조회 응답 오류", e);
            return ResponseEntity.ok().body(new DefaultResponse(ResultCode.SYSTEM_ERROR));
        }
    }


    @PostMapping(value = "/form/{fid}/update")
    public ResponseEntity<RootResponse> updateForm(@RequestHeader("Authorization") String token, @PathVariable("fid") Long fid,
                                     @Valid @RequestBody FormUpdateRequest request) {
        try {
            final String pattern = "yyyyMMdd";
            log.info("■ 1. 폼 상세 정보 조회 요청 성공");
            AuthUser authUser = new JwtTokenAuthUser(token);
            final Timestamp beginDt = TimeUtils.getTimeStamp(request.getBeginDt(), pattern);
            final Timestamp endDt = TimeUtils.getTimeStamp(request.getEndDt(), pattern);

            FormDataDto formDataDto = FormDataDto.builder()
                    .fid(fid)
                    .type(request.getType())
                    .title(request.getTitle())
                    .detail(request.getDetail())
                    .beginDt(beginDt)
                    .endDt(endDt)
                    .questionCount(request.getQuestionCount() == request.getQuestion().size() ? request.getQuestionCount() : request.getQuestion().size())
                    .status(request.getStatus())
                    .maxRespondent(request.getMaxRespondent())
                    .logoUrl(request.getLogoUrl())
                    .themeUrl(request.getThemeUrl())
                    .aid(authUser.getId())
                    .build(); // 폼 데이터

            List<QuestionDataDto> questionDataDtos = request.getQuestion().stream().map(it -> QuestionDataDto.builder()
                    .type(it.getType())
                    .order(it.getOrder())
                    .title(it.getTitle())
                    .placeholder(it.getPlaceholder())
                    .imageUrl(it.getImageUrl())
                    .detail(it.getDetail())
                    .exampleDetail(it.getExampleDetail())
                    .count(it.getCount())
                    .answer(it.getAnswer())
                    .build()).sorted(Comparator.comparing(QuestionDataDto::getOrder)).collect(Collectors.toList()); // 질문 데이터 order 순 정렬
            formService.updateForm(formDataDto, questionDataDtos); // 업데이트 처리
            return ResponseEntity.ok().body(new DefaultResponse());
        } catch (FormException e) {
            log.error("■ 폼 상세 정보 조회 응답 오류", e);
            return ResponseEntity.ok().body(new DefaultResponse(e.getCode()));
        } catch (Exception e) {
            log.error("■ 폼 상세 정보 조회 응답 오류", e);
            return ResponseEntity.ok().body(new DefaultResponse(ResultCode.SYSTEM_ERROR));
        }
    }


    /**
     * 유저 화면 데이터 제공
     *
     * @param token
     * @param fid
     * @return
     */
    @GetMapping(value = "/form/paper")
    public ResponseEntity<RootResponse> findPaper(@RequestHeader(value = "Authorization", required = false) String token,
                                                  @RequestParam @NotBlank @Min(0) Integer type,
                                                  @RequestParam @NotBlank @Min(0) Long fid,
                                                  @RequestParam @NotBlank String key) {
        try {
            log.info("■ 1. 유저 화면 정보 조회 요청 성공");
            AuthUser authUser = new JwtTokenAuthUser(token);

            FormTbEntity formTbEntity = formService.findPaper(FormDataDto.builder()
                            .fid(fid)
                            .type(type)
                            .key(key)
                            .aid(authUser.getId())
                    .build()); // 폼 상세 조회
            List<QuestionTbEntity> questionTbEntities = formService.findQuestions(fid); // 질문 리스트 조회
            log.info("■ 4. 유저 화면 정보 조회 응답 성공");
            final String separator = "\\|";

            List<FindPaperResponse.FormDetailQuestionResponse> question = questionTbEntities.stream()
                    .map(it -> FindPaperResponse.FormDetailQuestionResponse
                            .builder()
                            .qid(it.getQid())
                            .type(it.getQuestionType())
                            .order(it.getOrder())
                            .title(it.getTitle())
                            .placeholder(it.getQuestionPlaceholder())
                            .imageUrl(it.getImageUrl())
                            .count(it.getCount())
                            .detail(StringUtils.isNotBlank(it.getQuestionDetail()) ? it.getQuestionDetail().split(separator) : null)
                            .exampleDetail(StringUtils.isNotBlank(it.getQuestionExampleDetail()) ? it.getQuestionExampleDetail().split(separator) : null)
                            .answer(StringUtils.isNotBlank(it.getQuizAnswer()) ? it.getQuizAnswer().split(separator) : null)
                            .build())

                    .collect(Collectors.toList());;
            return ResponseEntity.ok().body(FindPaperResponse
                    .builder()
                            .fid(formTbEntity.getFid())
                            .type(formTbEntity.getFormType())
                            .title(formTbEntity.getTitle())
                            .detail(formTbEntity.getFormDetail())
                            .maxRespondent(formTbEntity.getMaxRespondent())
                            .logoUrl(formTbEntity.getLogoUrl())
                            .themeUrl(formTbEntity.getThemeUrl())
                            .question(question)
                            .worker(authUser.getId() == formTbEntity.getAid())
                    .build());
        } catch (FormException e) {
            log.error("■ 유저 화면 정보 조회 응답 오류, {}", e.getCode().getMsg());
            return ResponseEntity.ok().body(new DefaultResponse(e.getCode()));
        } catch (Exception e) {
            log.error("■ 유저 화면 정보 조회 응답 오류", e);
            return ResponseEntity.ok().body(new DefaultResponse(ResultCode.SYSTEM_ERROR));
        }
    }



}
