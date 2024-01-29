package com.kr.formdang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FormDelFlagEnum {

    NOT_DEL(0),
    DEL(1),

    ;

    private int code;
}
