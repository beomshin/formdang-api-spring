package com.kr.formdang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FormOrderEnum {

    RECENT(0),
    MANY_RESPONSE(1),
    LAST(2),

    ;

    private int code;
}
