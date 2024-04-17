package com.kr.formdang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FormStatusEnum {

    TEMP_STATUS(0),
    NORMAL_STATUS(1),

    ;

    private int code;
}
