package com.kr.formdang.external.kakao;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Getter
public class KakaoProp {

    @Value("${kakao.auth.url}")
    private String kakaoAuthUrl;

    @Value("${kakao.login.url}")
    private String kakaoLoginUrl;

    @Value("${kakao.redirect.login.uri}")
    private String kakaoRedirectLoginUri;

    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Value("${kakao.secret}")
    private String kakaoSecret;

    // Google 로그인 URL 생성 로직
    public String kakaoInitUrl() {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", getKakaoClientId());
        params.put("redirect_uri", getKakaoRedirectLoginUri());
        params.put("response_type", "code");

        String paramStr = params.entrySet().stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));

        return getKakaoLoginUrl()
                + "/oauth/authorize"
                + "?"
                + paramStr;
    }

}
