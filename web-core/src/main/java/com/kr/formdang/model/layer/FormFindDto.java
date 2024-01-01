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

    public boolean isAllType() {
        final Integer ALL = 99;
        return this.type.equals(ALL);
    }

    public boolean isAllStatus() {
        final Integer ALL = 99;
        return this.status.equals(ALL);
    }

    public boolean isProgressStatus() {
        final Integer ALL = 0;
        return this.status.equals(ALL);
    }

    public boolean isEndStatus() {
        final Integer ALL = 1;
        return this.status.equals(ALL);
    }

    public boolean isTempStatus() {
        final Integer ALL = 2;
        return this.status.equals(ALL);
    }

}
