package com.kr.formdang.external.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kr.formdang.external.HttpFormClient;
import com.kr.formdang.external.dto.RequestClient;
import com.kr.formdang.external.prop.GoogleProp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleClient implements HttpFormClient {

    private final RestTemplate snsApiRestTemplate;
    private final ObjectMapper apiObjectMapper;
    private final GoogleProp googleProp;

    @Override
    public <T> T requestToken(Object content, Class<T> tClass) throws JsonProcessingException {
        try {
            if (!(content instanceof RequestClient)) throw new ClassCastException("요청 컨텐츠 class cast exception");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<?> httpRequestEntity = new HttpEntity<>(content, headers);

            log.info("■ 구글 로그인 요청 : [{}]", httpRequestEntity);
            ResponseEntity<String> apiResponseJson = snsApiRestTemplate.postForEntity(googleProp.getGoogleAuthUrl() + "/token", httpRequestEntity, String.class); // 구글 토큰 API 요청
            log.info("■ 구글 로그인 응답 : [{}]", apiResponseJson);

            return apiObjectMapper.readValue(apiResponseJson.getBody(), tClass);
        }  catch (RestClientException e) {
            log.error("[구글 로그인 토큰 발급 요청 REST Exception 오류 발생]", e);
            throw e;
        } catch (Exception e) {
            log.error("[구글 로그인 토큰 발급 요청 미지정 오류 발생]", e);
            throw e;
        }
    }

    @Override
    public <T> T requestUserInfo(String token, Class<T> tClass) throws JsonProcessingException {
        try {
            String requestUrl = UriComponentsBuilder.fromHttpUrl(googleProp.getGoogleAuthUrl() + "/tokeninfo").queryParam("id_token", token).toUriString(); // 구글 사용자 정보 요청
            log.info("■ 구글 로그인 유저 정보 요청 : [{}]", requestUrl);
            String resultJson = snsApiRestTemplate.getForObject(requestUrl, String.class);
            log.info("■ 구글 로그인 유저 정보 응답 : [{}]", resultJson);
            return apiObjectMapper.readValue(resultJson, tClass);
        }  catch (RestClientException e) {
            log.error("[구글 로그인 유저 정보 발급 요청 REST Exception 오류 발생]", e);
            throw e;
        } catch (Exception e) {
            log.error("[구글 로그인 유저 정보 발급 요청 미지정 오류 발생]", e);
            throw e;
        }
    }
}
