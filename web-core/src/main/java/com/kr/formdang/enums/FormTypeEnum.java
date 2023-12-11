package com.kr.formdang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FormTypeEnum {

    INSPECTION_TYPE(0),
    QUIZ_TYPE(1),

    ;

    private int code;
}
