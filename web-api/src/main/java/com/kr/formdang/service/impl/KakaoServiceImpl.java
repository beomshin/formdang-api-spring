package com.kr.formdang.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kr.formdang.external.google.GoogleLoginRequestDto;
import com.kr.formdang.external.kakao.KakaoLoginDto;
import com.kr.formdang.external.kakao.KakaoLoginRequestDto;
import com.kr.formdang.external.kakao.KakaoLoginResponseDto;
import com.kr.formdang.external.kakao.KakaoProp;
import com.kr.formdang.service.KakaoService;
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

    @Override
    public KakaoLoginDto kakaoOAuth(KakaoLoginRequestDto requestDto) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", requestDto.getClientId());
        params.add("client_secret", requestDto.getClientSecret());
        params.add("grant_type", requestDto.getGrantType());
        params.add("redirect_uri", requestDto.getRedirectUri());
        params.add("code", requestDto.getCode());

        ResponseEntity<String> apiResponseJson = restTemplate.postForEntity(kakaoProp.getKakaoLoginUrl() + "/oauth/token", params, String.class);

        // ObjectMapper를 통해 String to Object로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // NULL이 아닌 값만 응답받기(NULL인 경우는 생략)
        KakaoLoginResponseDto kakaoLoginResponseDto = objectMapper.readValue(apiResponseJson.getBody(), new TypeReference<KakaoLoginResponseDto>() {});

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(kakaoLoginResponseDto.getAccessToken());
        HttpEntity<GoogleLoginRequestDto> httpRequestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(kakaoProp.getKakaoAuthUrl() + "/v2/user/me", httpRequestEntity, String.class);
        Map<String, Object> jsonResponse = objectMapper.readValue(response.getBody(), Map.class);
        log.debug("{}", jsonResponse);


        if (jsonResponse != null) {
            KakaoLoginDto userInfoDto = objectMapper.convertValue(jsonResponse, new TypeReference<KakaoLoginDto>() {});
            return userInfoDto;
        } else {
            throw new Exception("Kakao OAuth failed!");
        }
    }
}
