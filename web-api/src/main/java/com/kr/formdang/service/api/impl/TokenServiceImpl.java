package com.kr.formdang.service.api.impl;

import com.kr.formdang.model.external.auth.JwtTokenRequest;
import com.kr.formdang.model.external.auth.JwtTokenResponse;
import com.kr.formdang.service.api.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final RestTemplate commonRestTemplate;

    @Value("${formdang.url.auth-issue}")
    private String formdang_auth_issue;

    @Override
    public JwtTokenResponse getLoginToken(String id, String key) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<JwtTokenRequest> httpRequestEntity = new HttpEntity<>(new JwtTokenRequest(id, key), headers);
            log.debug("[인증토큰 요청] ===> [{}]", httpRequestEntity.getBody());
            ResponseEntity<JwtTokenResponse> response = commonRestTemplate.postForEntity(formdang_auth_issue, httpRequestEntity, JwtTokenResponse.class);
            log.debug("[인증토큰 응답] ===> STATUS: [{}], BODY: [{}]", response.getStatusCode(), response.getBody() );
            return response.getBody();
        } catch (Exception e) {
            log.error("{}", e);
            return null;
        }
    }
}
