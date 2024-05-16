package com.kr.formdang.external.impl;

import com.kr.formdang.exception.FormHttpException;
import com.kr.formdang.exception.ResultCode;
import com.kr.formdang.external.HttpFormClient;
import com.kr.formdang.external.dto.auth.JwtTokenRequest;
import com.kr.formdang.external.prop.FormProp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class AuthClient implements HttpFormClient {

    private final RestTemplate commonRestTemplate;
    private final FormProp formProp;

    @Override
    public <T> T requestToken(Object content, Class<T> tClass) throws FormHttpException {
        try {
            if (!(content instanceof JwtTokenRequest)) throw new ClassCastException("요청 컨텐츠 class cast exception");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ((JwtTokenRequest) content).setAuth_key(formProp.getSecretKey());
            HttpEntity<?> httpRequestEntity = new HttpEntity<>(content, headers);
            log.info("■ 폼당폼당 인증서버 토큰 요청 정보: [{}]", httpRequestEntity.getBody());
            ResponseEntity<T> response = commonRestTemplate.postForEntity(formProp.getIssueUrl(), httpRequestEntity, tClass);
            log.info("■ 폼당폼당 인증서버 토큰 응답 정보: [{}]", response);
            return response.getBody();
        } catch (RestClientException e) {
            log.error("[폼당 인증서버 토큰 발급 요청 REST Exception 오류 발생]", e);
            throw new FormHttpException(ResultCode.NETWORK_ERROR);
        } catch (Exception e) {
            log.error("[폼당 인증서버 토큰 발급 요청 미지정 오류 발생]", e);
            throw e;
        }
    }

    @Override
    public <T> T requestUserInfo(String token,  Class<T> tClass) throws IllegalAccessException {
        throw new IllegalAccessException();
    }
}
