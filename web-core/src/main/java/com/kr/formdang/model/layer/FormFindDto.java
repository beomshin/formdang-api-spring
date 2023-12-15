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

    public FormFindDto(Integer page, Integer type, Long aid) {
        this.page = page;
        this.type = type;
        this.aid = aid;
    }

    public boolean isAllType() {
        final Integer ALL = 99;
        return this.type.equals(ALL);
    }
}
