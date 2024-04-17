package com.kr.formdang.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FormTypeEnum {

    SURVEY_TYPE(0),
    QUIZ_TYPE(1),
    ALL(99)

    ;

    private int code;
}
