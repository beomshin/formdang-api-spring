package com.kr.formdang.model.layer;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class FormFindDto {

    private Integer page;
    private Integer type;

    public FormFindDto(Integer page, Integer type) {
        this.page = page;
        this.type = type;
    }
}
