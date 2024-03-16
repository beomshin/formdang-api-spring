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
    FAIL_FILE_CONDITION("30005", "파일 업로드 미부합 조건"),
    NOT_FIND_FORM("30006", "폼 상세정보 조회 실패"),
    REFUSE_ALREADY_START_FORM("30007", "설문 시작 폼으로 수정 거부"),
    REFUSE_ALREADY_DELETE_FORM("30008", "삭제 된 폼으로 수정 거부"),
    REFUSE_ALREADY_END_FORM("30009", "종료 된 폼으로 수정 거부"),
    NOT_FIND_QUESTIONS("30010", "폼 질문 리스트 조회 실패"),
    FAIL_UPLOAD_PROFILE("30011", "프로필 이미지 업로드 실패"),
    NOT_START_FORM("30012", "설문 시작이 되지않은 폼으로 접근 금지"),
    DELETE_FORM("30013", "삭제 된 폼으로 접근 금지"),
    END_FORM("30014", "종료 된 폼으로 접근 금지"),
    NOT_LOGIN_GROUP_FORM("30015", "폼 로그인 필요"),
    IS_NOT_GROUP_FORM_USER("30016", "그룹 폼 권한 미유저"),
    IS_MAX_RESPONSE("30017", "설문 최대 응답자 수 초과"),
    IS_NOT_RIGHT_DATE("30018", "설문 가능한 날짜가 아닙니다."),
    NOT_EXIST_PARAM("9991", "파라미터 누락 오류"),
    NOT_EXIST_HEADER("9992", "헤더 누락 오류"),
    NOT_EXIST_TOKEN("9993", "토큰 누락 오류"),
    IO_EXCEPTION("9994", "입출력 오류"),
    BIND_ERROR("9995", "파라미터 검증 오류"),
    HTTP_MESSAGE_NOT_READABLE_ERROR("9996","잘못된 자료형 요청 오류"),
    NOT_FOUND_PAGE("9997", "잘못된 API PATH 요청 오류"),
    PARAMETER_ERROR("9998", "요청 파라미터 오류"),
    SYSTEM_ERROR("9999", "미지정 오류"),
    FAIL_VALIDATE_TOKEN("17002", "토큰 인증 실패"),

    ;

    private final String code;
    private final String msg;

}
