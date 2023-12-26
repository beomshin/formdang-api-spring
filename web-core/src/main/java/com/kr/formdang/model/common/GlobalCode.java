package com.kr.formdang.model.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GlobalCode {

    SUCCESS("0", "성공"),
    FAIL_SAVE_ADMIN("30001", "관리자 계정 저장 실패"),
    NOT_UNIQUE_ADMIN("30002", "관리자 아이디 중복 계정 저장 실패"),
    FAIL_SUBMIT_FORM("30003", "폼 생성 실패"),
    NOT_EXIST_FILE("30004", "파일 미존재"),
    FAIL_FILE_CONDITION("30005", "파일 업로드 조건에 부합하지 않습니다."),
    NOT_EXIST_PARAM("9993", "파라미터 누락 오류"),
    NOT_EXIST_HEADER("9993", "헤더 누락 오류"),
    NOT_EXIST_TOKEN("9994", "토큰 누락 오류"),
    BIND_ERROR("9995", "파라미터 검증 오류"),
    HTTP_MESSAGE_NOT_READABLE_ERROR("9996","잘못된 자료형 요청 오류"),
    NOT_FOUND_PAGE("9997", "잘못된 API PATH 요청 오류"),
    PARAMETER_ERROR("9998", "요청 파라미터 오류"),
    SYSTEM_ERROR("9999", "미지정 오류"),

    ;

    private final String code;
    private final String msg;

}
