package com.kr.formdang.prop;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class LoginProp {

    @Value("${formdang.success-login}")
    private String successUrl;
    @Value("${formdang.success-paper-login}")
    private String successPaperUrl;
    @Value("${formdang.fail-login}")
    private String failUrl;
    @Value("${formdang.auth-issue.url}")
    private String issueUrl;

}
