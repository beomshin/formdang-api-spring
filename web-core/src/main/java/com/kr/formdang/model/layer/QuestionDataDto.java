package com.kr.formdang.model.layer;

import com.kr.formdang.model.net.request.FormSubmitRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class QuestionDataDto {

    private Integer type; // 질문 타입 ( 0: 주관식, 1: 객관식 )
    private Integer order; // 순번
    private String title; // 질문 제목
    private String placeholder; // 질문 placeholder
    private String imageUrl; // 이미지 url
    private String detail[]; // 질문 상세 내용 (주관식 x, 객관식 JSON 형태 질문)
    private Integer count; // 질문 개수
    private String answer[]; // 질문 정답

    public QuestionDataDto(FormSubmitRequest.FormSubmitQuestionRequest request) {
        this.type = request.getType();
        this.order = request.getOrder();
        this.title = request.getTitle();
        this.placeholder = request.getPlaceholder();
        this.imageUrl = request.getImageUrl();
        this.detail = request.getDetail();
        this.count = request.getCount();
        this.answer = request.getAnswer();
    }
}
