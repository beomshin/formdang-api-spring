package com.kr.formdang.model.response.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kr.formdang.entity.AnswerTbEntity;
import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.entity.QuestionTbEntity;
import com.kr.formdang.exception.ResultCode;
import com.kr.formdang.model.response.AbstractResponse;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FindPaperResponse extends AbstractResponse {

    private Long fid;
    private Integer type; // 폼 타입 ( 0: 설문, 1: 퀴즈, 2: 쪽지시험 )
    private String title; // 폼 제목
    private String detail; // 폼 설명
    private Integer maxRespondent; // 인원 제한수 ( 0: 제한 없음, 1~ 제한인원)
    private String logoUrl; // 로그 url
    private String themeUrl; // 테마 url
    private List<FindPaperResponse.FormDetailQuestionResponse> question;
    private boolean worker;
    private boolean submit;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp beginDt;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp endDt;

    public FindPaperResponse(FormTbEntity form, List<QuestionTbEntity> questions, List<AnswerTbEntity> answers) {
        this.fid = form.getFid();
        this.type = form.getFormType();
        this.title = form.getTitle();
        this.detail = form.getFormDetail();
        this.maxRespondent = form.getMaxRespondent();
        this.logoUrl = form.getLogoUrl();
        this.themeUrl = form.getThemeUrl();
        this.beginDt = form.getBeginDt();
        this.endDt = form.getEndDt();
        this.question = questions.stream().map(FormDetailQuestionResponse::new).collect(Collectors.toList());
        if (answers != null) {
            Map<Long, AnswerTbEntity> answerMap = answers.stream().collect(Collectors.toMap(AnswerTbEntity::getQid, (it) -> it, (v1, v2) -> v1));
            for (int i=0; i < questions.size(); i++) {
                if (answerMap.containsKey(question.get(i).getQid())) {
                    question.get(i).putAnswer(answerMap.get(questions.get(i).getQid()));
                }
            }
            this.submit = true;
        }
    }

    public FindPaperResponse ss(FormTbEntity formTb) {
        FindPaperResponse response = new FindPaperResponse();
        response.setBeginDt(formTb.getBeginDt());
        response.setEndDt(formTb.getEndDt());
        super.resultCode = ResultCode.IS_NOT_RIGHT_DATE.getCode();
        super.resultMsg = ResultCode.IS_NOT_RIGHT_DATE.getMsg();
        return response;
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
        private String sAnswer;
        private String mAnswer;
        private int okFlag;

        public FormDetailQuestionResponse(QuestionTbEntity questionTb) {
            this.qid = questionTb.getQid();
            this.type = questionTb.getQuestionType();
            this.order = questionTb.getOrder();
            this.title = questionTb.getTitle();
            this.placeholder = questionTb.getQuestionPlaceholder();
            this.imageUrl = questionTb.getImageUrl();
            this.count = questionTb.getCount();
            this.detail = StringUtils.split(questionTb.getQuestionDetail());
            this.exampleDetail = StringUtils.split(questionTb.getQuestionExampleDetail());
            this.answer = StringUtils.split(questionTb.getQuizAnswer());
        }

        public void putAnswer(AnswerTbEntity answer) {
            this.sAnswer = answer.getSAnswer();
            this.mAnswer = answer.getMAnswer();
            this.okFlag = answer.getOkFlag();
        }
    }

    public void setError(ResultCode resultCode) {
        super.resultCode = resultCode.getCode();
        super.resultMsg = resultCode.getMsg();
    }

}
