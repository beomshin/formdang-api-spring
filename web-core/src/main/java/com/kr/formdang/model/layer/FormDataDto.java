package com.kr.formdang.model.layer;

import com.kr.formdang.model.net.annotation.BeforeDateValid;
import com.kr.formdang.model.net.annotation.DateValid;
import com.kr.formdang.model.net.request.FormSubmitRequest;
import com.kr.formdang.utils.TimeUtils;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class FormDataDto {

    private Long aid; // 관리자 아이디
    private Integer type; // 폼 타입 ( 0: 설문, 1: 퀴즈, 2: 쪽지시험 )
    private String title; // 폼 제목
    private String detail; // 폼 설명
    private Timestamp beginDt; // 시작일 (yyyyMMdd)
    private Timestamp endDt; // 종료일 (yyyyMMdd)
    private Integer questionCount; // 질문 개수
    private Integer status; // 폼 상태 (0: 임시저장, 1: 등록)
    private Integer maxRespondent; // 인원 제한수 ( 0: 제한 없음, 1~ 제한인원)
    private String logoUrl; // 로그 url
    private String themeUrl; // 테마 url

    public FormDataDto(FormSubmitRequest request, Long aid, String pattern) throws ParseException {
        this.type = request.getType();
        this.title = request.getTitle();
        this.detail = request.getDetail();
        this.beginDt = TimeUtils.getTimeStamp(request.getBeginDt(), pattern);
        this.endDt = TimeUtils.getTimeStamp(request.getEndDt(), pattern);
        this.questionCount = request.getQuestionCount() == request.getQuestion().size() ? request.getQuestionCount() : request.getQuestion().size();
        this.status = request.getStatus();
        this.maxRespondent = request.getStatus();
        this.maxRespondent = request.getMaxRespondent();
        this.logoUrl = request.getLogoUrl();
        this.themeUrl = request.getThemeUrl();
        this.aid = aid;
    }

}
