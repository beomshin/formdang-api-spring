package com.kr.formdang.controller.form;

import com.kr.formdang.entity.AnswerTbEntity;
import com.kr.formdang.model.*;
import com.kr.formdang.model.response.FormResponse;
import com.kr.formdang.model.response.FailResponse;
import com.kr.formdang.model.response.SuccessResponse;
import com.kr.formdang.model.response.form.*;
import com.kr.formdang.model.user.AnonymousUser;
import com.kr.formdang.model.user.FormUser;
import com.kr.formdang.entity.AdminSubTbEntity;
import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.entity.QuestionTbEntity;
import com.kr.formdang.enums.PageEnum;
import com.kr.formdang.exception.FormException;
import com.kr.formdang.exception.ResultCode;
import com.kr.formdang.model.request.form.FormSubmitRequest;
import com.kr.formdang.model.request.form.FormUpdateRequest;
import com.kr.formdang.service.auth.AuthService;
import com.kr.formdang.service.form.FormService;
import com.kr.formdang.utils.FormFlagUtils;
import com.kr.formdang.utils.StrUtils;
import com.kr.formdang.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FormController {

    private final FormService formService;
    private final AuthService authService;

    /**
     * 폼 등록 API
     */
    @PostMapping(value = "/form/submit")
    public ResponseEntity<FormResponse> submitForm(
            @Valid @RequestBody FormSubmitRequest request,
            @RequestHeader("Authorization") String token
    ) {

        log.info("■ 1. 폼 작성하기 요청 성공");
        FormUser formUser = authService.generateAuthUser(token);

        // 폼 엔티티
        FormTbEntity formTbEntity = FormTbEntity.builder()
                .aid(formUser.getId())
                .title(request.getTitle())
                .formType(request.getType())
                .formDetail(request.getDetail())
                .beginDt(TimeUtils.getTimeStamp(request.getBeginDt()))
                .endDt(TimeUtils.getTimeStamp(request.getEndDt()))
                .maxRespondent(request.getMaxRespondent())
                .questionCount(request.getQuestionCount() == request.getQuestion().size() ? request.getQuestionCount() : request.getQuestion().size())
                .logoUrl(request.getLogoUrl())
                .themeUrl(request.getThemeUrl())
                .status(request.getStatus())
                .build(); // 폼 엔티티 생성

        // 질문 리스트 엔티티
        List<QuestionTbEntity> questionTbEntities = request.getQuestion().stream()
                .map(it ->
                        QuestionTbEntity.builder()
                                .order(it.getOrder())
                                .questionType(it.getType())
                                .title(it.getTitle())
                                .questionPlaceholder(it.getPlaceholder())
                                .imageUrl(it.getImageUrl())
                                .count(it.getCount())
                                .questionDetail(StrUtils.joining(it.getDetail()))
                                .questionExampleDetail(StrUtils.joining(it.getExampleDetail()))
                                .quizAnswer(StrUtils.joining(it.getAnswer()))
                                .build()
                )
                .collect(Collectors.toList()); // 질문 엔티티 리스트 생성

        try {
            log.info("■ 2. 폼 테이블 등록");
            FormTbEntity formTb = formService.submitForm(formTbEntity, questionTbEntities); // 폼 저장
            FormResponse response = new SubmitFormResponse(formTb.getFid());
            log.info("■ 3. 폼 작성하기 응답 성공");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            log.error("■ 폼 테이블 등록 실패: " + e);
            FormResponse response = new FailResponse(ResultCode.FAIL_SUBMIT_FORM);
            log.info("■ 3. 폼 작성하기 응답 성공");
            return ResponseEntity.ok().body(response);
        }
    }

    /**
     * 폼 리스트 조회 API
     * 페이징 처리 ( 0~ )
     * 타입 처리 ( 설문, 퀴즈 .. )
     * 상태 처리 ( 진행, 종료, 임시 )
     */
    @GetMapping(value = "/form/list/find")
    public ResponseEntity<FormResponse> findFormList(
            @RequestParam @NotBlank @Min(value = 0, message = "페이지 개수는 0이상입니다.") Integer page,
            @RequestParam @NotBlank @Min(0) Integer type,
            @RequestParam @NotBlank @Min(0) Integer status,
            @RequestParam @NotBlank @Min(0) Integer order,
            @RequestHeader("Authorization") String token)
    {

        log.info("■ 1. 폼리스트 조회 요청 성공");
        FormUser formUser = authService.generateAuthUser(token);

        PageRequest pageRequest = PageRequest.of(page, PageEnum.PAGE_12.getNum()); // 페이징 처리 생성
        SqlFormParam sqlFormParam = SqlFormParam.builder() // 조회 파라미터 생성
                    .aid(formUser.getId())
                    .offset(pageRequest.getOffset())
                    .pageSize(pageRequest.getPageSize())
                    .type(FormFlagUtils.type(type))
                    .delFlag(FormFlagUtils.delFlag(status))
                    .endFlag(FormFlagUtils.endFlag(status))
                    .status(FormFlagUtils.status(status))
                    .order(FormFlagUtils.order(order))
                .build();

        log.info("■ 2. 폼 리스트 조회 쿼리 시작");
        Page<FormTbEntity> pages = formService.findFormList(sqlFormParam, pageRequest); // 폼 리스트 조회

        log.info("■ 3. 종합 분석 정보 조회 쿼리 시작");
        AdminSubTbEntity adminSubTb = formService.analyzeForm(formUser.getId()); // 종합 정보 조회

        log.info("■ 4. 폼리스트 조회 응답 성공");
        FormResponse response = FindFormListResponse.builder()
                .totalElements(pages.getTotalElements())
                .totalPage(pages.getTotalPages())
                .curPage(pages.getNumber())
                .analyze(FindFormListResponse.Analyze.builder()
                            .inspectionCnt(adminSubTb.getInspectionCnt())
                            .quizCnt(adminSubTb.getQuizCnt())
                            .inspectionRespondentCnt(adminSubTb.getInspectionRespondentCnt())
                            .quizRespondentCnt(adminSubTb.getQuizRespondent_cnt())
                        .build())
                .list(pages.getContent().stream().map(it -> FindFormListResponse.Forms.builder()
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
     * 종합 분석 데이터 조회
     * 퀴즈, 설문 생성 개수, 응답 개수
     */
    @GetMapping(value = "/form/analyze")
    public ResponseEntity<FormResponse> analyzeForm(@RequestHeader("Authorization") String token) {

        log.info("■ 1. 종합 분석 조회 요청 성공");
        FormUser formUser = authService.generateAuthUser(token);

        log.info("■ 2. 종합 분석 정보 조회 쿼리 시작");
        AdminSubTbEntity adminSubTb = formService.analyzeForm(formUser.getId()); // 종합 정보 조회

        log.info("■ 3. 종합 분석 조회 응답 성공");
        FormResponse response = AnalyzeFormResponse.builder()
                    .inspectionCnt(adminSubTb.getInspectionCnt())
                    .quizCnt(adminSubTb.getQuizCnt())
                    .inspectionRespondentCnt(adminSubTb.getInspectionRespondentCnt())
                    .quizRespondentCnt(adminSubTb.getQuizRespondent_cnt())
                .build();

        return ResponseEntity.ok().body(response);

    }

    /**
     * 폼 상세 정보 조회하기
     */
    @GetMapping(value = "/form/detail/{fid}/find")
    public ResponseEntity<FormResponse> findFormDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable("fid") Long fid) throws FormException
    {

            log.info("■ 1. 폼 상세 정보 조회 요청 성공");
            FormUser formUser = authService.generateAuthUser(token);

            log.info("■ 2. 폼 상세 정보 조회 쿼리 시작");
            FormTbEntity formTbEntity = formService.findForm(formUser.getId(), fid); // 폼 상세 조회

            log.info("■ 3. 폼 질문 리스트 조회 쿼리 시작");
            List<QuestionTbEntity> questionTbEntities = formService.findQuestions(fid); // 질문 리스트 조회

            log.info("■ 4. 폼 상세 정보 조회 응답 성공");
            FormResponse response = FindFormDetailResponse.builder()
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
                        .question(questionTbEntities.stream().map(
                                it -> FindFormDetailResponse.FormDetailQuestionResponse.builder()
                                            .qid(it.getQid())
                                            .type(it.getQuestionType())
                                            .order(it.getOrder())
                                            .title(it.getTitle())
                                            .placeholder(it.getQuestionPlaceholder())
                                            .imageUrl(it.getImageUrl())
                                            .count(it.getCount())
                                            .detail(StrUtils.split(it.getQuestionDetail()))
                                            .exampleDetail(StrUtils.split(it.getQuestionExampleDetail()))
                                            .answer(StrUtils.split(it.getQuizAnswer()))
                                        .build())
                                .collect(Collectors.toList()))
                    .build();

            return ResponseEntity.ok().body(response);
    }

    /**
     * 폼 업데이트
     */
    @PostMapping(value = "/form/{fid}/update")
    public ResponseEntity<FormResponse> updateForm(
            @RequestHeader("Authorization") String token,
            @PathVariable("fid") Long fid,
            @Valid @RequestBody FormUpdateRequest request) throws FormException
    {

            log.info("■ 1. 폼 정보 업데이트 요청 성공");
            FormUser formUser = authService.generateAuthUser(token);

            FormTbEntity formTbEntity = FormTbEntity.builder()
                        .fid(fid)
                        .formType(request.getType())
                        .title(request.getTitle())
                        .formDetail(request.getDetail())
                        .beginDt(TimeUtils.getTimeStamp(request.getBeginDt()))
                        .endDt(TimeUtils.getTimeStamp(request.getEndDt()))
                        .questionCount(request.getQuestionCount() == request.getQuestion().size() ? request.getQuestionCount() : request.getQuestion().size())
                        .status(request.getStatus())
                        .maxRespondent(request.getMaxRespondent())
                        .logoUrl(request.getLogoUrl())
                        .themeUrl(request.getThemeUrl())
                        .aid(formUser.getId())
                    .build();


            List<QuestionTbEntity> questionTbEntities = request.getQuestion().stream()
                    .map(it -> QuestionTbEntity.builder()
                            .questionType(it.getType())
                            .order(it.getOrder())
                            .title(it.getTitle())
                            .questionPlaceholder(it.getPlaceholder())
                            .imageUrl(it.getImageUrl())
                            .count(it.getCount())
                            .questionDetail(StrUtils.joining(it.getDetail()))
                            .questionExampleDetail(StrUtils.joining(it.getExampleDetail()))
                            .quizAnswer(StrUtils.joining(it.getAnswer()))
                            .build()
                    ).sorted(Comparator.comparing(QuestionTbEntity::getOrder)).collect(Collectors.toList()); // 질문 데이터 order 순 정렬

            log.info("■ 2. 폼 정보 업데이트 시작");
            formService.modifyForm(formTbEntity, questionTbEntities); // 업데이트 처리

            log.info("■ 3. 폼 정보 업데이트 응답 성공");
            FormResponse response = new SuccessResponse();

            return ResponseEntity.ok().body(response);
    }


    /**
     * 유저 화면 데이터 제공
     */
    @GetMapping(value = "/form/paper")
    public ResponseEntity<FormResponse> findPaper(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam @NotBlank @Min(0) Integer type,
            @RequestParam @NotBlank @Min(0) Long fid,
            @RequestParam @NotBlank String key) throws FormException
    {
        
        FormUser formUser = null;
        try {
            formUser = authService.generateAuthUser(token);
            log.info("■ 1. 유저 화면 정보 조회 요청 성공");
        } catch (Exception e) {
            formUser = new AnonymousUser();
            log.info("■ 1. 비유저 화면 정보 조회 요청 성공");
        }

        FormTbEntity formTbEntity = FormTbEntity.builder()
                    .fid(fid)
                    .formType(type)
                    .aid(formUser.getId())
                .build();

        log.info("■ 2. 폼 상세 정보 조회 시작");
        FormTbEntity form = formService.findPaper(formTbEntity, key); // 폼 상세 조회

        log.info("■ 3. 폼 제출 여부 조회");
        List<AnswerTbEntity> answers = formService.findAnswers(formTbEntity); // 폼 제출 여부 조회

        log.info("■ 4. 폼 질문 리스트 조회 쿼리 시작");
        List<QuestionTbEntity> questions = formService.findQuestions(fid); // 질문 리스트 조회

        log.info("■ 5. 유저 화면 정보 조회 응답 성공");
        FindPaperResponse response = new FindPaperResponse(form, questions, answers);
        response.setWorker(formUser.getId() == form.getAid());

        if (formUser instanceof AnonymousUser) {
            response.setError(ResultCode.NOT_LOGIN_GROUP_FORM);
        }

        return ResponseEntity.ok().body(response);
    }



}
