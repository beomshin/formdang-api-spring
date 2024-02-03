package com.kr.formdang.service.api.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kr.formdang.model.external.google.GoogleLoginRequestDto;
import com.kr.formdang.model.external.kakao.KakaoLoginDto;
import com.kr.formdang.model.external.kakao.KakaoLoginRequestDto;
import com.kr.formdang.model.external.kakao.KakaoLoginResponseDto;
import com.kr.formdang.model.external.kakao.KakaoProp;
import com.kr.formdang.service.api.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoServiceImpl implements KakaoService {

    private final KakaoProp kakaoProp;
    private final RestTemplate snsApiRestTemplate;
    private final ObjectMapper apiObjectMapper;

    @Override
    public KakaoLoginDto kakaoOAuth(KakaoLoginRequestDto requestDto) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", requestDto.getClientId());
        params.add("client_secret", requestDto.getClientSecret());
        params.add("grant_type", requestDto.getGrantType());
        params.add("redirect_uri", requestDto.getRedirectUri());
        params.add("code", requestDto.getCode());

        log.info("■ 2. 카카오 로그인 구글 토큰 요청");
        log.info("■ 카카오 토큰 요청 정보 : [{}]", params);
        ResponseEntity<String> apiResponseJson = snsApiRestTemplate.postForEntity(kakaoProp.getKakaoLoginUrl() + "/oauth/token", params, String.class); // 카카오 토큰 API 요청
        log.info("■ 카카오 토큰 응답 정보 : [{}]", apiResponseJson);

        KakaoLoginResponseDto kakaoLoginResponseDto = apiObjectMapper.readValue(apiResponseJson.getBody(), new TypeReference<KakaoLoginResponseDto>() {});

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(kakaoLoginResponseDto.getAccessToken());
        HttpEntity<GoogleLoginRequestDto> httpRequestEntity = new HttpEntity<>(null, headers);

        log.info("■ 3. 카카오 로그인 구글 토큰 요청");
        log.info("■ 카카오 사용자 정보 요청 정보 : [{}]", httpRequestEntity);
        ResponseEntity<String> response = snsApiRestTemplate.postForEntity(kakaoProp.getKakaoAuthUrl() + "/v2/user/me", httpRequestEntity, String.class); // 카카오 사용자 정보 요청
        log.info("■ 카카오 정보 응답 응답 : [{}]", response);

        Map<String, Object> jsonResponse = apiObjectMapper.readValue(response.getBody(), Map.class);

        if (jsonResponse != null) {
            KakaoLoginDto userInfoDto = apiObjectMapper.convertValue(jsonResponse, new TypeReference<KakaoLoginDto>() {});
            return userInfoDto;
        } else {
            log.error("■ 카카오 사용자 정보 누락");
            throw new Exception("Kakao OAuth failed!");
        }
    }
}
