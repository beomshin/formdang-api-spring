package com.kr.formdang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminTypeEnum {

    NORMAL_TYPE(0),

    KAKAO_TYPE(1),

    ;

    private int code;
}
