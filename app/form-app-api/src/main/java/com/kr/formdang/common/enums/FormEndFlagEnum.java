package com.kr.formdang.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FormEndFlagEnum {

    PROGRESS(0),
    END(1),

    ;

    private int code;
}
