package com.kr.formdang.service.api.impl;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kr.formdang.model.external.google.GoogleLoginDto;
import com.kr.formdang.model.external.google.GoogleLoginRequestDto;
import com.kr.formdang.model.external.google.GoogleLoginResponseDto;
import com.kr.formdang.model.external.google.GoogleProp;
import com.kr.formdang.service.api.GoogleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleServiceImpl implements GoogleService {

    private final GoogleProp googleProp;
    private final RestTemplate snsApiRestTemplate;
    private final ObjectMapper apiObjectMapper;

    @Override
    public GoogleLoginDto googleOAuth(GoogleLoginRequestDto googleLoginRequestDto) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GoogleLoginRequestDto> httpRequestEntity = new HttpEntity<>(googleLoginRequestDto, headers);


        log.info("■ 2. 구글 로그인 구글 토큰 요청");
        log.info("■ 구글 토큰 요청 정보 : [{}]", httpRequestEntity);
        ResponseEntity<String> apiResponseJson = snsApiRestTemplate.postForEntity(googleProp.getGoogleAuthUrl() + "/token", httpRequestEntity, String.class); // 구글 토큰 API 요청
        log.info("■ 구글 토큰 응답 정보 : [{}]", apiResponseJson);

        GoogleLoginResponseDto googleLoginResponse = apiObjectMapper.readValue(apiResponseJson.getBody(), new TypeReference<GoogleLoginResponseDto>() {});

        String requestUrl = UriComponentsBuilder.fromHttpUrl(googleProp.getGoogleAuthUrl() + "/tokeninfo").queryParam("id_token", googleLoginResponse.getIdToken()).toUriString(); // 구글 사용자 정보 요청

        log.info("■ 3. 구글 로그인 구글 토큰 요청");
        log.info("■ 구글 사용자 정보 요청 정보 : [{}]", requestUrl);
        String resultJson = snsApiRestTemplate.getForObject(requestUrl, String.class);
        log.info("■ 구글 사용자 정보 응답 응답 : [{}]", resultJson);

        if(resultJson != null) {
            GoogleLoginDto userInfoDto = apiObjectMapper.readValue(resultJson, new TypeReference<GoogleLoginDto>() {});
            return userInfoDto;
        }
        else {
            log.error("■ 구글 사용자 정보 누락");
            throw new Exception("Google OAuth failed!");
        }


    }
}
