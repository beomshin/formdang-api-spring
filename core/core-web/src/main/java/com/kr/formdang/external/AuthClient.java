package com.kr.formdang.external;

import com.kr.formdang.external.dto.RequestClient;
import com.kr.formdang.external.dto.ResponseClient;
import com.kr.formdang.external.dto.auth.JwtTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthClient {

    private final RestTemplate commonRestTemplate;

    @Value("${formdang.auth-issue.url}")
    private String formdang_auth_issue;

    public ResponseClient requestToken(RequestClient requestClient) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<RequestClient> httpRequestEntity = new HttpEntity<>(requestClient, headers);
            log.info("■ 폼당폼당 인증서버 토큰 요청 정보: [{}]", httpRequestEntity.getBody());
            ResponseEntity<JwtTokenResponse> response = commonRestTemplate.postForEntity(formdang_auth_issue, httpRequestEntity, JwtTokenResponse.class);
            log.info("■ 폼당폼당 인증서버 토큰 응답 정보: [{}]", response);
            return response.getBody();
        } catch (RestClientException e) {
            log.error("[폼당 인증서버 토큰 발급 요청 IO Exception 오류 발생]", e);
            throw e;
        } catch (Exception e) {
            log.error("[폼당 인증서버 토큰 발급 요청 미지정 오류 발생]", e);
            throw e;
        }
    }


}
