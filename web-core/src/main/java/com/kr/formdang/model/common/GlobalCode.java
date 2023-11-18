package com.kr.formdang.model.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GlobalCode {

    SUCCESS("0", "성공"),
    BIND_ERROR("9995", "파라미터 검증 오류"),
    HTTP_MESSAGE_NOT_READABLE_ERROR("9996","잘못된 형식 API 요청 오류"),
    NOT_FOUND_PAGE("9997", "잘못된 API PATH 요청 오류"),
    PARAMETER_ERROR("9998", "요청 파라미터 오류"),
    SYSTEM_ERROR("9999", "미지정 오류"),

    ;

    private final String code;
    private final String msg;

}
