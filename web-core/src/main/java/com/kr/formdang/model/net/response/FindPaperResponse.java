package com.kr.formdang.model.net.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.entity.QuestionTbEntity;
import com.kr.formdang.model.root.DefaultResponse;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class FindPaperResponse extends DefaultResponse {

    private Long fid;
    private Integer type; // 폼 타입 ( 0: 설문, 1: 퀴즈, 2: 쪽지시험 )
    private String title; // 폼 제목
    private String detail; // 폼 설명
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "Asia/Seoul")
    private Timestamp beginDt; // 시작일 (yyyyMMdd)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "Asia/Seoul")
    private Timestamp endDt; // 종료일 (yyyyMMdd)
    private Integer questionCount; // 질문 개수
    private Integer status; // 폼 상태 (0: 임시저장, 1: 등록)
    private Integer endFlag; // 종료 플래그 ( 0: 미종료, 1: 종료)
    private Integer delFlag; // 삭제 플래그 ( 0: 미삭제, 1: 삭제)
    private Integer answerCount; // 질문 응답자 수
    private Integer maxRespondent; // 인원 제한수 ( 0: 제한 없음, 1~ 제한인원)
    private String logoUrl; // 로그 url
    private String themeUrl; // 테마 url
    private List<FindPaperResponse.FormDetailQuestionResponse> question;


    public FindPaperResponse(FormTbEntity formTbEntity, List<QuestionTbEntity> questionTbEntities) {
        this.fid = formTbEntity.getFid();
        this.type = formTbEntity.getFormType();
        this.title = formTbEntity.getTitle();
        this.detail = formTbEntity.getFormDetail();
        this.beginDt = formTbEntity.getBeginDt();
        this.endDt = formTbEntity.getEndDt();
        this.questionCount = formTbEntity.getQuestionCount();
        this.status = formTbEntity.getStatus();
        this.endFlag = formTbEntity.getEndFlag();
        this.delFlag = formTbEntity.getDelFlag();
        this.answerCount = formTbEntity.getAnswerCount();
        this.maxRespondent = formTbEntity.getMaxRespondent();
        this.logoUrl = formTbEntity.getLogoUrl();
        this.themeUrl = formTbEntity.getThemeUrl();
        this.question = questionTbEntities.stream().map(FormDetailQuestionResponse::new).collect(Collectors.toList());;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FormDetailQuestionResponse {

        private Long qid;
        private Integer type; // 질문 타입 ( 0: 주관식, 1: 객관식 )
        private Integer order; // 순번
        private String title; // 질문 제목
        private String placeholder; // 질문 placeholder
        private String imageUrl; // 이미지 url
        private String[] detail; // 질문 상세 내용
        private String[] exampleDetail; // 질문 보기 상세 내용
        private Integer count; // 질문 개수
        private String[] answer; // 질문 정답

        public FormDetailQuestionResponse(QuestionTbEntity questionTbEntity) {
            final String separator = "\\|";
            this.qid = questionTbEntity.getQid();
            this.type = questionTbEntity.getQuestionType();
            this.order = questionTbEntity.getOrder();
            this.title = questionTbEntity.getTitle();
            this.placeholder = questionTbEntity.getQuestionPlaceholder();
            this.imageUrl = questionTbEntity.getImageUrl();
            this.detail = StringUtils.isNotBlank(questionTbEntity.getQuestionDetail()) ? questionTbEntity.getQuestionDetail().split(separator) : null;
            this.exampleDetail = StringUtils.isNotBlank(questionTbEntity.getQuestionExampleDetail()) ? questionTbEntity.getQuestionExampleDetail().split(separator) : null;
            this.count = questionTbEntity.getCount();
            this.answer = StringUtils.isNotBlank(questionTbEntity.getQuizAnswer()) ? questionTbEntity.getQuizAnswer().split(separator) : null;
        }

    }

}
