package com.kr.formdang.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kr.formdang.external.dto.ResponseClient;
import com.kr.formdang.external.dto.google.GoogleTokenRequest;
import com.kr.formdang.external.dto.kakao.KakaoLoginResponse;
import com.kr.formdang.external.dto.kakao.KakaoTokenResponse;
import com.kr.formdang.prop.KakaoProp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class KakaoClient {

    private final RestTemplate snsApiRestTemplate;
    private final KakaoProp kakaoProp;
    private final ObjectMapper apiObjectMapper;

    public ResponseClient requestToken(MultiValueMap<String, String> params) throws JsonProcessingException {
        try {
            log.info("■ 카카오 로그인 토큰 요청 : [{}]", params);
            ResponseEntity<String> apiResponseJson = snsApiRestTemplate.postForEntity(kakaoProp.getKakaoLoginUrl() + "/oauth/token", params, String.class); // 카카오 토큰 API 요청
            log.info("■ 카카오 로그인 토큰 응답 : [{}]", apiResponseJson);
            return apiObjectMapper.readValue(apiResponseJson.getBody(), new TypeReference<KakaoTokenResponse>() {});
        }  catch (RestClientException e) {
            log.error("[카카오 로그인 토큰 발급 요청 IO Exception 오류 발생]", e);
            throw e;
        } catch (Exception e) {
            log.error("[카카오 로그인 토큰 발급 요청 미지정 오류 발생]", e);
            throw e;
        }
    }

    public ResponseClient requestUserInfo(String token) throws JsonProcessingException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity<GoogleTokenRequest> httpRequestEntity = new HttpEntity<>(null, headers);

            log.info("■ 카카오 로그인 유저 정보 요청 : [{}]", httpRequestEntity);
            ResponseEntity<String> response = snsApiRestTemplate.postForEntity(kakaoProp.getKakaoAuthUrl() + "/v2/user/me", httpRequestEntity, String.class); // 카카오 사용자 정보 요청
            log.info("■ 카카오 로그인 유저 정보 응답 : [{}]", response);
            return apiObjectMapper.convertValue(apiObjectMapper.readValue(response.getBody(), Map.class), new TypeReference<KakaoLoginResponse>() {});
        }  catch (RestClientException e) {
            log.error("[카카오 로그인 유저 정보 발급 요청 IO Exception 오류 발생]", e);
            throw e;
        } catch (Exception e) {
            log.error("[카카오 로그인 유저 정보 발급 요청 미지정 오류 발생]", e);
            throw e;
        }
    }

}
