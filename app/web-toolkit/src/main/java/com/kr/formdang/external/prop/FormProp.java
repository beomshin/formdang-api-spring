package com.kr.formdang.external.prop;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class FormProp {

    @Value("${formdang.success-login}")
    private String successUrl; // 폼당폼당 관리자 페이지 접근 로그인 성공 페이지
    @Value("${formdang.success-paper-login}")
    private String successPaperUrl; // 폼당폼당 설문 페이지 접근 로그인 성공 페이지
    @Value("${formdang.fail-login}")
    private String failUrl; // 폼당폼당 로그인 실패 페이지
    @Value("${formdang.auth-issue.url}")
    private String issueUrl; // 폼당폼당 에러 페이지
    @Value("${formdang.secret-key}")
    private String secretKey; // 인증 서버 접근 키

}
