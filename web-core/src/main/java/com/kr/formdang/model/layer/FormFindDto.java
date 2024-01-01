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

    public FormFindDto(Integer page, Integer type, Long aid, Integer status) {
        this.page = page;
        this.type = type;
        this.aid = aid;
        this.status = status;
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

}
