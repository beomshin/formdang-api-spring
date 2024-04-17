package com.kr.formdang.dto;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class FormDataDto {

    private Long fid;
    private Long aid; // 관리자 아이디
    private Integer type; // 폼 타입 ( 0: 설문, 1: 퀴즈, 2: 쪽지시험 )
    private String title; // 폼 제목
    private String detail; // 폼 설명
    private Timestamp beginDt; // 시작일 (yyyyMMdd)
    private Timestamp endDt; // 종료일 (yyyyMMdd)
    private Integer questionCount; // 질문 개수
    private Integer status; // 폼 상태 (0: 임시저장, 1: 등록)
    private Integer maxRespondent; // 인원 제한수 ( 0: 제한 없음, 1~ 제한인원)
    private String logoUrl; // 로그 url
    private String themeUrl; // 테마 url
    private String key; // 유저 화면 폼 키
    private String token; // 헤더 토큰

}
