package com.kr.formdang.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PageEnum {

    PAGE_4(4),
    PAGE_12(12),

    ;

    private int num;
}
