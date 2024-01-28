package com.kr.formdang.model.layer;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class FormFindDto {

    private Integer page;
    private Integer type;
    private Long aid;
    private Integer status;
    private Integer order;

    public FormFindDto(Integer page, Integer type, Long aid, Integer status, Integer order) {
        this.page = page;
        this.type = type;
        this.aid = aid;
        this.status = status;
        this.order = order;
    }

    public boolean isAllType() { // 전체 타입
        final int ALL = 99;
        return this.type.equals(ALL);
    }

    public boolean isSurvey() { // 설문 타입
        final int SURVEY = 0;
        return this.type.equals(SURVEY);
    }

    public boolean isQuiz() { // 퀴즈 타입
        final int QUIZ = 1;
        return this.type.equals(QUIZ);
    }

    public boolean isAllStatus() { // 상태 전체 조회
        final int ALL = 99;
        return this.status.equals(ALL);
    }

    public boolean isProgressStatus() { // 진행 상태 조회
        final int PROGRESS = 0;
        return this.status.equals(PROGRESS);
    }

    public boolean isEndStatus() { // 종료 상태 조회
        final int END = 1;
        return this.status.equals(END);
    }

    public boolean isTempStatus() { // 임시 상태 조회
        final int TEMP = 2;
        return this.status.equals(TEMP);
    }

    public boolean isDelStatus() { // 삭제 상태 조회
        final int DEL = 2;
        return this.status.equals(DEL);
    }

    public boolean isRecent() {
        final int RECENT = 0;
        return this.order.equals(RECENT);
    }


}
