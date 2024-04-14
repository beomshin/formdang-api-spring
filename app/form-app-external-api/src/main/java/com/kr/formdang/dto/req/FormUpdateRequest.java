package com.kr.formdang.dto.req;

import com.kr.formdang.dto.annotation.ArrayLenValid;
import com.kr.formdang.dto.annotation.BeforeDateValid;
import com.kr.formdang.dto.annotation.DateValid;
import com.kr.formdang.root.DefaultRequest;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class FormUpdateRequest extends DefaultRequest {

    @NotNull(message = "폼 타입이 누락되었습니다.")
    private Integer type; // 폼 타입 ( 0: 설문, 1: 퀴즈, 2: 쪽지시험 )

    @NotBlank(message = "폼 제목이 누락되었습니다.")
    @Size(min = 1, max = 256, message = "폼 제목 길이가 초과되었습니다.")
    private String title; // 폼 제목

    @NotBlank(message = "폼 설명이 누락되었습니다.")
    @Size(min = 1, max = 512, message = "폼 설명 길이가 초과되었습니다.")
    private String detail; // 폼 설명

    @NotBlank(message = "폼 시작일이 누락되었습니다.")
    @DateValid
    @BeforeDateValid
    private String beginDt; // 시작일 (yyyyMMdd)

    @NotBlank(message = "폼 종료일이 누락되었습니다.")
    @DateValid @BeforeDateValid
    private String endDt; // 종료일 (yyyyMMdd)

    @NotNull(message = "폼 질문 개수 값이 누락되었습니다.")
    @Max(20)
    private Integer questionCount; // 질문 개수

    @NotNull(message = "폼 상태 값이 누락되었습니다.")
    private Integer status; // 폼 상태 (0: 임시저장, 1: 등록)

    private Integer maxRespondent; // 인원 제한수 ( 0: 제한 없음, 1~ 제한인원)

    private String logoUrl; // 로그 url

    private String themeUrl; // 테마 url

    @Valid @NotNull(message = "질문 리스트가 누락되었습니다.")
    private List<FormSubmitQuestionRequest> question;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FormSubmitQuestionRequest {

        @NotNull(message = "질문 타입이 누락되었습니다.")
        private Integer type; // 질문 타입 ( 0: 주관식, 1: 객관식 )

        @NotNull(message = "질문 순번이 누락되었습니다.")
        private Integer order; // 순번

        @NotNull(message = "질문 제목이 누락되었습니다.")
        @Size(min = 1, max = 256, message = "질문 제목 길이가 초과되었습니다.")
        private String title; // 질문 제목

        @Size(max = 64, message = "질문 창 내용 길이가 초과되었습니다.")
        private String placeholder; // 질문 placeholder

        private String imageUrl; // 이미지 url

        @ArrayLenValid
        private String[] detail; // 질문 상세 내용

        @ArrayLenValid
        private String[] exampleDetail; // 질문 보기 상세 내용

        private Integer count; // 질문 개수

        private String[] answer; // 질문 정답

    }

}
